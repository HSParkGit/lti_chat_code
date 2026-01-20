package kr.lineedu.lms.feature.chatSystem.api;

import kr.lineedu.lms.config.jwt.JwtPrincipal;
import kr.lineedu.lms.feature.chatSystem.api.dto.feign.CanvasUserDto;
import kr.lineedu.lms.feature.chatSystem.api.dto.request.*;
import kr.lineedu.lms.feature.chatSystem.api.dto.response.*;
import kr.lineedu.lms.feature.chatSystem.api.feign.CanvasChatClient;
import kr.lineedu.lms.feature.chatSystem.domain.*;
import kr.lineedu.lms.global.error.dto.ErrorCode;
import kr.lineedu.lms.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import kr.lineedu.lms.feature.chatSystem.api.dto.response.InviteLinkResponse;
import kr.lineedu.lms.config.constant.CanvasConstantUtil;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ParticipantRepository participantRepository;
    private final CanvasChatClient canvasChatClient;
    private final SimpMessagingTemplate messagingTemplate;

    // Simple in-memory cache for Canvas user info to avoid repeated API calls
    private final ConcurrentHashMap<Long, CanvasUserDto> userCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Boolean> failedUserIds = new ConcurrentHashMap<>();

    private Long getCurrentUserId() {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getLmsUserId(); // Canvas user ID
    }

    public List<UserResponse> searchUsersInCourse(SearchUsersRequest request, Long asUserId, Long currentUserId) {
        
        List<CanvasUserDto> canvasUsers = canvasChatClient.searchCourseUsers(
            request.getCourseId(),
            request.getSearchTerm(),
            asUserId
        );
        
        return canvasUsers.stream()
            // Filter out the current authenticated user from search results
            .filter(user -> !user.getId().equals(currentUserId))
            .map(user -> UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .shortName(user.getShortName())
                .build())
            .collect(Collectors.toList());
    }

    public ChatResponse createDirectChat(CreateDirectChatRequest request) {
        Long currentUserId = getCurrentUserId();
        Long otherUserId = request.getOtherUserId();

        // Check if direct chat already exists (even if current user previously deleted it)
        Chat existingChat = chatRepository.findDirectChatsBetweenUsersIncludingDeleted(currentUserId, otherUserId)
            .stream()
            .findFirst()
            .orElse(null);

        if (existingChat != null) {
            // If current user had soft-deleted, restore and stamp deletedAt to hide old messages
            participantRepository.findByChatIdAndUserId(existingChat.getChatId(), currentUserId)
                .ifPresent(p -> {
                    if (Boolean.TRUE.equals(p.getDeleted())) {
                        p.setDeleted(false);
                        p.setDeletedAt(LocalDateTime.now());
                        participantRepository.save(p);
                    }
                });
            return buildChatResponse(existingChat, currentUserId);
        }

        // No existing chat; create new direct chat
        Chat chat = Chat.builder()
            .type(Chat.ChatType.direct)
            .build();
        chat = chatRepository.save(chat);

        // Add participants
        Participant participant1 = Participant.builder()
            .chatId(chat.getChatId())
            .userId(currentUserId)
            .role(Participant.ParticipantRole.member)
            .build();
        
        Participant participant2 = Participant.builder()
            .chatId(chat.getChatId())
            .userId(otherUserId)
            .role(Participant.ParticipantRole.member)
            .build();

        // Save all participants at once and flush to ensure they're immediately visible
        List<Participant> savedParticipants = participantRepository.saveAll(List.of(participant1, participant2));
        participantRepository.flush();
        
        log.info("Created direct chat - chatId: {}, participants saved: userId1={}, userId2={}", 
            chat.getChatId(), currentUserId, otherUserId);
        log.info("Saved participants count: {}, chatId verification: {}", 
            savedParticipants.size(), savedParticipants.stream()
                .map(p -> "userId=" + p.getUserId() + ", chatId=" + p.getChatId())
                .collect(Collectors.joining(", ")));

        return buildChatResponse(chat, currentUserId);
    }

    public ChatResponse createGroupChat(CreateGroupChatRequest request) {
        Long currentUserId = getCurrentUserId();

        // Generate invite link token
        String inviteLink = UUID.randomUUID().toString();

        Chat chat = Chat.builder()
            .type(Chat.ChatType.group)
            .name(request.getName())
            .inviteLink(inviteLink)
            .build();
        chat = chatRepository.save(chat);

        // Add creator as admin
        Participant creator = Participant.builder()
            .chatId(chat.getChatId())
            .userId(currentUserId)
            .role(Participant.ParticipantRole.creator)
            .build();
        participantRepository.save(creator);

        // Add other participants
        for (Long userId : request.getUserIds()) {
            if (!userId.equals(currentUserId)) {
                Participant participant = Participant.builder()
                    .chatId(chat.getChatId())
                    .userId(userId)
                    .role(Participant.ParticipantRole.member)
                    .build();
                participantRepository.save(participant);
            }
        }

        return buildChatResponse(chat, currentUserId);
    }

    @CacheEvict(cacheNames = {"chat:messages", "chat:unreadCounts"}, allEntries = true)
    public MessageResponse sendMessage(SendMessageRequest request) {
        Long currentUserId = getCurrentUserId();
        
        log.info("Attempting to send message - chatId: {}, userId: {}", request.getChatId(), currentUserId);

        // First check if chat exists
        Optional<Chat> chatOpt = chatRepository.findById(request.getChatId());
        if (chatOpt.isEmpty()) {
            log.warn("Chat not found - chatId: {}", request.getChatId());
            throw new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION, 
                "Chat not found: " + request.getChatId());
        }

        // Check if user is a participant
        Optional<Participant> participantOpt = participantRepository.findByChatIdAndUserIdAndDeletedFalse(
            request.getChatId(), currentUserId);
        if (participantOpt.isEmpty()) {
            log.warn("User is not a participant - chatId: {}, userId: {}", request.getChatId(), currentUserId);
            // Verify if chat exists and list all participants for debugging
            List<Participant> allParticipants = participantRepository.findByChatId(request.getChatId());
            log.warn("Chat exists but user not found. Chat participants: {}", 
                allParticipants.stream().map(p -> "userId=" + p.getUserId()).collect(Collectors.joining(", ")));
            throw new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION, 
                "User is not a participant in this chat");
        }

        Chat chat = chatOpt.get();

        // Validate replyToMessageId if provided
        if (StringUtils.hasText(request.getReplyToMessageId())) {
            Message replyToMessage = messageRepository.findById(request.getReplyToMessageId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MESSAGE_EXCEPTION, 
                    "Reply message not found: " + request.getReplyToMessageId()));
            
            // Verify that the reply message belongs to the same chat
            if (!replyToMessage.getChatId().equals(request.getChatId())) {
                throw new BusinessException(ErrorCode.NOT_FOUND_MESSAGE_EXCEPTION, 
                    "Reply message does not belong to this chat");
            }
        }

        Message.MessageType messageType = Message.MessageType.valueOf(request.getMessageType());

        Message message = Message.builder()
            .chatId(request.getChatId())
            .senderId(currentUserId)
            .content(request.getContent())
            .messageType(messageType)
            .replyToMessageId(request.getReplyToMessageId())
            .build();

        message = messageRepository.save(message);

        // Update chat's last message
        chat.updateLastMessage(message.getMessageId());
        chatRepository.save(chat);

        // Send via WebSocket
        List<Participant> participants = participantRepository.findByChatIdAndDeletedFalse(chat.getChatId());
        MessageResponse messageResponse = buildMessageResponse(message);

        log.info("Sending WebSocket message - chatId: {}, chatType: {}, participants: {}",
            chat.getChatId(), chat.getType(),
            participants.stream().map(p -> p.getUserId().toString()).collect(Collectors.joining(",")));

        if (chat.getType() == Chat.ChatType.group) {
            // Broadcast once to the group topic
            String destination = "/group/chat/" + chat.getChatId();
            log.info("Broadcasting to group destination: {}", destination);
            messagingTemplate.convertAndSend(destination, messageResponse);
        } else {
            // Direct chat: unicast to participants, skip muted
            for (Participant participant : participants) {
                if (Boolean.TRUE.equals(participant.getMuted())) {
                    log.debug("Skipping muted participant - chatId: {}, userId: {}", chat.getChatId(), participant.getUserId());
                    continue;
                }
                String destination = "/user/" + participant.getUserId() + "/queue/messages";
                log.info("Sending to destination: {}", destination);
                messagingTemplate.convertAndSend(destination, messageResponse);
            }
        }

        return messageResponse;
    }

    public List<ChatResponse> getAllConversations() {
        Long currentUserId = getCurrentUserId();
        List<Chat> chats = chatRepository.findAllByUserId(currentUserId);
        
        return chats.stream()
            .map(chat -> buildChatResponse(chat, currentUserId))
            .collect(Collectors.toList());
    }

    public List<ChatResponse> getUnreadConversations() {
        Long currentUserId = getCurrentUserId();
        List<Chat> chats = chatRepository.findAllByUserId(currentUserId);
        
        return chats.stream()
            .map(chat -> buildChatResponse(chat, currentUserId))
            .filter(chatResponse -> chatResponse.getUnreadCount() != null && chatResponse.getUnreadCount() > 0)
            .collect(Collectors.toList());
    }

    // @Cacheable disabled - Page<T> serialization issue with Redis
    // TODO: Re-enable with proper Redis serializer configuration for Page objects
    public Page<MessageResponse> getMessages(String chatId, int page, int size) {
        Long currentUserId = getCurrentUserId();

        // Verify user is participant
        chatRepository.findByIdAndUserId(chatId, currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION, 
                "Chat not found or user is not a participant"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages;

        // If user had previously deleted, hide messages before deletedAt
        Optional<Participant> participantOpt = participantRepository.findByChatIdAndUserId(chatId, currentUserId);
        if (participantOpt.isPresent() && participantOpt.get().getDeletedAt() != null) {
            messages = messageRepository.findByChatIdAndTimestampAfterOrderByTimestampDesc(
                chatId,
                participantOpt.get().getDeletedAt(),
                pageable
            );
        } else {
            messages = messageRepository.findByChatIdOrderByTimestampDesc(chatId, pageable);
        }

        return messages.map(this::buildMessageResponse);
    }

    @Cacheable(
        cacheNames = "chat:unreadCounts",
        key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getPrincipal().lmsUserId"
    )
    public List<UnreadCountResponse> getUnreadCounts() {
        Long currentUserId = getCurrentUserId();
        List<Chat> chats = chatRepository.findAllByUserId(currentUserId);

        return chats.stream()
            .map(chat -> {
                Long unreadCount = calculateUnreadCount(chat.getChatId(), currentUserId);
                return UnreadCountResponse.builder()
                    .chatId(chat.getChatId())
                    .unreadCount(unreadCount)
                    .build();
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Calculate unread message count for a user in a chat
     * Messages sent by the user are not counted as unread
     */
    private Long calculateUnreadCount(String chatId, Long userId) {
        // Get participant to find their last read message
        Optional<Participant> participantOpt = participantRepository.findByChatIdAndUserIdAndDeletedFalse(chatId, userId);
        
        LocalDateTime lastReadTimestamp = null;
        LocalDateTime deletedAt = null;
        
        if (participantOpt.isPresent()) {
            Participant participant = participantOpt.get();
            String lastReadMessageId = participant.getLastReadMessageId();
            deletedAt = participant.getDeletedAt();
            
            if (lastReadMessageId != null) {
                // Get the timestamp of the last read message
                Optional<Message> lastReadMessage = messageRepository.findById(lastReadMessageId);
                if (lastReadMessage.isPresent()) {
                    lastReadTimestamp = lastReadMessage.get().getTimestamp();
                }
            }
            // If lastReadMessageId is null, lastReadTimestamp remains null (all messages are unread)
        }

        // If user previously deleted the chat, start unread count after deletedAt
        if (deletedAt != null) {
            if (lastReadTimestamp == null || deletedAt.isAfter(lastReadTimestamp)) {
                lastReadTimestamp = deletedAt;
            }
        }
        
        // Count messages after last read timestamp, excluding messages sent by the user
        return messageRepository.countUnreadMessagesByTimestamp(chatId, userId, lastReadTimestamp);
    }

    @CacheEvict(cacheNames = {"chat:messages", "chat:unreadCounts"}, allEntries = true)
    public void markAsRead(String chatId) {
        Long currentUserId = getCurrentUserId();
        
        Participant participant = participantRepository.findByChatIdAndUserIdAndDeletedFalse(chatId, currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION, 
                "Participant not found - user is not a member of this chat"));

        Message lastMessage = messageRepository.findFirstByChatIdOrderByTimestampDesc(chatId)
            .orElse(null);

        if (lastMessage != null) {
            participant.updateLastReadMessage(lastMessage.getMessageId());
            participantRepository.save(participant);
        }
    }

    @CacheEvict(cacheNames = {"chat:messages", "chat:unreadCounts"}, allEntries = true)
    public void muteChat(String chatId) {
        Long currentUserId = getCurrentUserId();

        Participant participant = participantRepository.findByChatIdAndUserIdAndDeletedFalse(chatId, currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION,
                "Participant not found - user is not a member of this chat"));

        participant.setMuted(true);
        participantRepository.save(participant);
    }

    @CacheEvict(cacheNames = {"chat:messages", "chat:unreadCounts"}, allEntries = true)
    public void unmuteChat(String chatId) {
        Long currentUserId = getCurrentUserId();

        Participant participant = participantRepository.findByChatIdAndUserIdAndDeletedFalse(chatId, currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION,
                "Participant not found - user is not a member of this chat"));

        participant.setMuted(false);
        participantRepository.save(participant);
    }

    @CacheEvict(cacheNames = {"chat:messages", "chat:unreadCounts"}, allEntries = true)
    public void deleteConversation(String chatId) {
        Long currentUserId = getCurrentUserId();

        Participant participant = participantRepository.findByChatIdAndUserId(chatId, currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION,
                "Chat not found or user is not a participant"));

        participant.setDeleted(true);
        participant.setDeletedAt(LocalDateTime.now());
        participantRepository.save(participant);
    }

    public InviteLinkResponse generateInviteLink(String chatId, Long courseId, Long toolId) {
        Long currentUserId = getCurrentUserId();
        
        Chat chat = chatRepository.findByIdAndUserId(chatId, currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION, 
                "Chat not found or user is not a participant"));
        
        // Only group chats can have invite links
        if (chat.getType() != Chat.ChatType.group) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, 
                "Invite links are only available for group chats");
        }
        
        // Check if user is admin or creator
        Participant participant = participantRepository.findByChatIdAndUserIdAndDeletedFalse(chatId, currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION, 
                "User is not a participant"));
        
        if (participant.getRole() != Participant.ParticipantRole.admin && 
            participant.getRole() != Participant.ParticipantRole.creator) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, 
                "Only admins and creators can generate invite links");
        }
        
        // Generate new invite link if doesn't exist
        if (chat.getInviteLink() == null || chat.getInviteLink().isEmpty()) {
            String inviteLink = UUID.randomUUID().toString();
            chat.setInviteLink(inviteLink);
            chatRepository.save(chat);
        }
        
        // Build Canvas external tool URL for sharing
        // Format: https://canvas-lms.lomtech.net/courses/{courseId}/external_tools/{toolId}?invite_link={inviteLink}
        String canvasBaseUrl = CanvasConstantUtil.CANVAS_URL;
        
        String inviteUrl = String.format("%s/courses/%d/external_tools/%d?invite_link=%s", 
            canvasBaseUrl, courseId, toolId, chat.getInviteLink());
        
        return InviteLinkResponse.builder()
            .inviteLink(chat.getInviteLink())
            .inviteUrl(inviteUrl)
            .build();
    }

    public ChatResponse joinGroupChatByInviteLink(String inviteLink) {
        Long currentUserId = getCurrentUserId();
        
        Chat chat = chatRepository.findByInviteLink(inviteLink)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION, 
                "Invalid invite link"));
        
        // Check if user is already a participant
        Optional<Participant> existingParticipant = participantRepository.findByChatIdAndUserId(
            chat.getChatId(), currentUserId);
        
        if (existingParticipant.isPresent()) {
            Participant participant = existingParticipant.get();
            if (Boolean.TRUE.equals(participant.getDeleted())) {
                participant.setDeleted(false);
                participantRepository.save(participant);
            }
            // User already in chat, return existing chat
            log.info("User {} already in group chat {}, returning existing chat", currentUserId, chat.getChatId());
            return buildChatResponse(chat, currentUserId);
        }
        
        // Add user as participant
        Participant participant = Participant.builder()
            .chatId(chat.getChatId())
            .userId(currentUserId)
            .role(Participant.ParticipantRole.member)
            .build();
        
        participantRepository.save(participant);
        
        log.info("User {} joined group chat {} via invite link", currentUserId, chat.getChatId());
        
        return buildChatResponse(chat, currentUserId);
    }

    public void removeParticipantFromGroupChat(String chatId, Long userIdToRemove) {
        Long currentUserId = getCurrentUserId();
        
        Chat chat = chatRepository.findByIdAndUserId(chatId, currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION, 
                "Chat not found or user is not a participant"));
        
        // Only group chats
        if (chat.getType() != Chat.ChatType.group) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, 
                "Cannot remove participants from direct chats");
        }
        
        // Get current user's participant record
        Participant currentUserParticipant = participantRepository.findByChatIdAndUserIdAndDeletedFalse(chatId, currentUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHAT_ROOM_EXCEPTION, 
                "User is not a participant"));
        
        // Check permissions: only admin or creator can remove participants
        if (currentUserParticipant.getRole() != Participant.ParticipantRole.admin && 
            currentUserParticipant.getRole() != Participant.ParticipantRole.creator) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, 
                "Only admins and creators can remove participants");
        }
        
        // Cannot remove creator
        Participant participantToRemove = participantRepository.findByChatIdAndUserId(chatId, userIdToRemove)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION, 
                "User is not a participant in this chat"));
        
        if (participantToRemove.getRole() == Participant.ParticipantRole.creator) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, 
                "Cannot remove the creator from the group chat");
        }
        
        // Remove participant
        participantRepository.delete(participantToRemove);
        
        log.info("User {} removed user {} from group chat {}", currentUserId, userIdToRemove, chatId);
    }

    private ChatResponse buildChatResponse(Chat chat, Long currentUserId) {
        List<Participant> participants = participantRepository.findByChatIdAndDeletedFalse(chat.getChatId());
        List<UserResponse> userResponses = participants.stream()
            .map(p -> {
                Long userId = p.getUserId();
                // Skip if we already know this user failed
                if (failedUserIds.containsKey(userId)) {
                    return UserResponse.builder().id(userId).build();
                }
                // Check cache first
                CanvasUserDto canvasUser = userCache.get(userId);
                if (canvasUser == null) {
                    try {
                        canvasUser = canvasChatClient.getUserById(userId, null);
                        userCache.put(userId, canvasUser);
                    } catch (Exception e) {
                        log.warn("Failed to fetch user {} from Canvas, will skip future attempts: {}", userId, e.getMessage());
                        failedUserIds.put(userId, true);
                        return UserResponse.builder().id(userId).build();
                    }
                }
                return UserResponse.builder()
                    .id(canvasUser.getId())
                    .name(canvasUser.getName())
                    .loginId(canvasUser.getLoginId())
                    .email(canvasUser.getEmail())
                    .avatarUrl(canvasUser.getAvatarUrl())
                    .shortName(canvasUser.getShortName())
                    .build();
            })
            .collect(Collectors.toList());

        MessageResponse lastMessageResponse = null;
        if (chat.getLastMessageId() != null) {
            Message lastMessage = messageRepository.findById(chat.getLastMessageId()).orElse(null);
            if (lastMessage != null) {
                lastMessageResponse = buildMessageResponse(lastMessage);
            }
        }

        // Calculate unread count
        Long unreadCount = calculateUnreadCount(chat.getChatId(), currentUserId);
        
        // Debug logging
        Optional<Participant> participant = participantRepository.findByChatIdAndUserIdAndDeletedFalse(chat.getChatId(), currentUserId);
        log.debug("Unread count calculation - chatId: {}, userId: {}, unreadCount: {}, lastReadMessageId: {}", 
            chat.getChatId(), currentUserId, unreadCount, 
            participant.map(Participant::getLastReadMessageId).orElse("NOT_FOUND"));

        return ChatResponse.builder()
            .chatId(chat.getChatId())
            .type(chat.getType().name())
            .name(chat.getName())
            .createdAt(chat.getCreatedAt())
            .lastMessage(lastMessageResponse)
            .participants(userResponses)
            .unreadCount(unreadCount)
            .inviteLink(chat.getInviteLink())
            .muted(participant.map(Participant::getMuted).orElse(false))
            .build();
    }

    private MessageResponse buildMessageResponse(Message message) {
        MessageResponse.MessageResponseBuilder builder = MessageResponse.builder()
            .messageId(message.getMessageId())
            .chatId(message.getChatId())
            .senderId(message.getSenderId())
            .content(message.getContent())
            .timestamp(message.getTimestamp())
            .messageType(message.getMessageType().name())
            .replyToMessageId(message.getReplyToMessageId());

        // Fetch sender details from Canvas with caching
        Long senderId = message.getSenderId();
        if (!failedUserIds.containsKey(senderId)) {
            CanvasUserDto sender = userCache.get(senderId);
            if (sender == null) {
                try {
                    sender = canvasChatClient.getUserById(senderId, null);
                    userCache.put(senderId, sender);
                } catch (Exception e) {
                    log.warn("Failed to fetch sender {} from Canvas, will skip future attempts: {}", senderId, e.getMessage());
                    failedUserIds.put(senderId, true);
                }
            }
            if (sender != null) {
                builder.senderName(sender.getName())
                       .senderAvatarUrl(sender.getAvatarUrl());
            }
        }

        if (message.getReplyToMessageId() != null) {
            Message replyTo = messageRepository.findById(message.getReplyToMessageId()).orElse(null);
            if (replyTo != null) {
                builder.replyToMessage(buildMessageResponse(replyTo));
            }
        }

        return builder.build();
    }
}

