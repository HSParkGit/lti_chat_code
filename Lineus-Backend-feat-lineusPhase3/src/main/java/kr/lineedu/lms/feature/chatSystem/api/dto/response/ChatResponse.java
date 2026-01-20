package kr.lineedu.lms.feature.chatSystem.api.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatResponse {
    private String chatId;
    private String type; // direct, group
    private String name;
    private LocalDateTime createdAt;
    private MessageResponse lastMessage;
    private List<UserResponse> participants;
    private Long unreadCount;
    private String inviteLink;
    private Boolean muted;
}

