package kr.lineedu.lms.feature.chatSystem.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "Participants",
    indexes = {
        @Index(name = "idx_participants_user_deleted", columnList = "user_id, deleted"),
        @Index(name = "idx_participants_chat_deleted", columnList = "chat_id, deleted")
    }
)
@Getter
@Setter
@NoArgsConstructor
@IdClass(ParticipantId.class)
public class Participant {

    @Id
    @Column(name = "chat_id", length = 36)
    private String chatId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ParticipantRole role;

    @Column(name = "last_read_message_id", length = 36)
    private String lastReadMessageId;

    @Column(name = "muted", nullable = false)
    private Boolean muted = false;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_read_message_id", insertable = false, updatable = false)
    private Message lastReadMessage;

    @PrePersist
    protected void onCreate() {
        if (joinedAt == null) {
            joinedAt = LocalDateTime.now();
        }
        if (role == null) {
            role = ParticipantRole.member;
        }
        if (muted == null) {
            muted = false;
        }
        if (deleted == null) {
            deleted = false;
        }
        if (deletedAt == null && Boolean.TRUE.equals(deleted)) {
            deletedAt = LocalDateTime.now();
        }
    }

    @Builder
    public Participant(String chatId, Long userId, LocalDateTime joinedAt, 
                       ParticipantRole role, String lastReadMessageId, Boolean muted, Boolean deleted) {
        this.chatId = chatId;
        this.userId = userId;
        this.joinedAt = joinedAt != null ? joinedAt : LocalDateTime.now();
        this.role = role != null ? role : ParticipantRole.member;
        this.lastReadMessageId = lastReadMessageId;
        this.muted = muted != null ? muted : false;
        this.deleted = deleted != null ? deleted : false;
    }

    public void updateLastReadMessage(String messageId) {
        this.lastReadMessageId = messageId;
    }

    public enum ParticipantRole {
        member, admin, creator
    }
}

