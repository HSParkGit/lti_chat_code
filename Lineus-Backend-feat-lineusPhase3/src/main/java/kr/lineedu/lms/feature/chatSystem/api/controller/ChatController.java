package kr.lineedu.lms.feature.chatSystem.api.controller;

import jakarta.validation.Valid;
import kr.lineedu.lms.config.jwt.JwtPrincipal;
import kr.lineedu.lms.feature.chatSystem.api.ChatService;
import kr.lineedu.lms.feature.chatSystem.api.dto.request.*;
import kr.lineedu.lms.feature.chatSystem.api.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/users/search")
    public ResponseEntity<List<UserResponse>> searchUsersInCourse(
            @RequestParam(value = "course_id") Long courseId,
            @RequestParam(value = "search_term") String searchTerm,
            @RequestParam(value = "as_user_id", required = false) Long asUserId) {
        Long currentUserId = getCurrentUserId();
        Long userIdToUse = asUserId != null ? asUserId : currentUserId;
        SearchUsersRequest request = new SearchUsersRequest();
        request.setCourseId(courseId);
        request.setSearchTerm(searchTerm);
        List<UserResponse> users = chatService.searchUsersInCourse(request, userIdToUse, currentUserId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/direct")
    public ResponseEntity<ChatResponse> createDirectChat(@Valid @RequestBody CreateDirectChatRequest request) {
        ChatResponse chat = chatService.createDirectChat(request);
        return ResponseEntity.ok(chat);
    }

    @PostMapping("/group")
    public ResponseEntity<ChatResponse> createGroupChat(@Valid @RequestBody CreateGroupChatRequest request) {
        ChatResponse chat = chatService.createGroupChat(request);
        return ResponseEntity.ok(chat);
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody SendMessageRequest request) {
        MessageResponse message = chatService.sendMessage(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ChatResponse>> getAllConversations() {
        List<ChatResponse> conversations = chatService.getAllConversations();
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/conversations/unread")
    public ResponseEntity<List<ChatResponse>> getUnreadConversations() {
        List<ChatResponse> unreadConversations = chatService.getUnreadConversations();
        return ResponseEntity.ok(unreadConversations);
    }

    @GetMapping("/conversations/{chatId}/messages")
    public ResponseEntity<Page<MessageResponse>> getMessages(
            @PathVariable String chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Page<MessageResponse> messages = chatService.getMessages(chatId, page, size);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<UnreadCountResponse>> getUnreadCounts() {
        List<UnreadCountResponse> unreadCounts = chatService.getUnreadCounts();
        return ResponseEntity.ok(unreadCounts);
    }

    @PostMapping("/conversations/{chatId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable String chatId) {
        chatService.markAsRead(chatId);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/conversations/{chatId}")
    public ResponseEntity<String> deleteConversation(@PathVariable String chatId) {
        chatService.deleteConversation(chatId);
        return ResponseEntity.ok("deleted");
    }

    @PostMapping("/conversations/{chatId}/mute")
    public ResponseEntity<String> muteChat(@PathVariable String chatId) {
        chatService.muteChat(chatId);
        return ResponseEntity.ok("muted");
    }

    @PostMapping("/conversations/{chatId}/unmute")
    public ResponseEntity<String> unmuteChat(@PathVariable String chatId) {
        chatService.unmuteChat(chatId);
        return ResponseEntity.ok("unmuted");
    }

    @GetMapping("/group/{chatId}/invite-link")
    public ResponseEntity<InviteLinkResponse> generateInviteLink(
            @PathVariable String chatId,
            @RequestParam(value = "course_id") Long courseId,
            @RequestParam(value = "tool_id") Long toolId) {
        InviteLinkResponse inviteLink = chatService.generateInviteLink(chatId, courseId, toolId);
        return ResponseEntity.ok(inviteLink);
    }

    @PostMapping("/join/{inviteLink}")
    public ResponseEntity<ChatResponse> joinGroupChatByInviteLink(@PathVariable String inviteLink) {
        ChatResponse chat = chatService.joinGroupChatByInviteLink(inviteLink);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/join")
    public ResponseEntity<ChatResponse> joinGroupChatByInviteLinkQuery(
            @RequestParam(value = "invite_link") String inviteLink) {
        ChatResponse chat = chatService.joinGroupChatByInviteLink(inviteLink);
        return ResponseEntity.ok(chat);
    }

    @DeleteMapping("/group/{chatId}/participants")
    public ResponseEntity<String> removeParticipant(
            @PathVariable String chatId,
            @Valid @RequestBody RemoveParticipantRequest request) {
        chatService.removeParticipantFromGroupChat(chatId, request.getUserId());
        return ResponseEntity.ok("Participant removed successfully");
    }

    private Long getCurrentUserId() {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getLmsUserId();
    }
}

