package kr.lineedu.lms.feature.chatSystem.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "Messages",
    indexes = {
        @Index(name = "idx_messages_chat_sender_time", columnList = "chat_id, sender_id, timestamp")
    }
)
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    @Column(name = "message_id", length = 36)
    private String messageId;

    @Column(name = "chat_id", nullable = false, length = 36)
    private String chatId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "message_type", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column(name = "reply_to_message_id", length = 36)
    private String replyToMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_message_id", insertable = false, updatable = false)
    private Message replyToMessage;

    @PrePersist
    protected void onCreate() {
        if (messageId == null) {
            messageId = UUID.randomUUID().toString();
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    @Builder
    public Message(String messageId, String chatId, Long senderId, String content, 
                   LocalDateTime timestamp, MessageType messageType, String replyToMessageId) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.messageType = messageType;
        this.replyToMessageId = replyToMessageId;
    }

    public enum MessageType {
        text, image, video, system
    }
}

