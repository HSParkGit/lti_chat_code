package kr.lineedu.lms.feature.chatSystem.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
    name = "Chats",
    indexes = {
        @Index(name = "idx_chats_invite_link_unique", columnList = "invite_link", unique = true)
    }
)
@Getter
@Setter
@NoArgsConstructor
public class Chat {

    @Id
    @Column(name = "chat_id", length = 36)
    private String chatId;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ChatType type;

    @Column(length = 255)
    private String name;

    @Column(name = "invite_link", length = 255)
    private String inviteLink;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_message_id", length = 36)
    private String lastMessageId;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id", insertable = false, updatable = false)
    private Message lastMessage;

    @PrePersist
    protected void onCreate() {
        if (chatId == null) {
            chatId = UUID.randomUUID().toString();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @Builder
    public Chat(String chatId, ChatType type, String name, String inviteLink, LocalDateTime createdAt, String lastMessageId) {
        this.chatId = chatId;
        this.type = type;
        this.name = name;
        this.inviteLink = inviteLink;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.lastMessageId = lastMessageId;
    }

    public void updateLastMessage(String messageId) {
        this.lastMessageId = messageId;
    }

    public enum ChatType {
        direct, group
    }
}

