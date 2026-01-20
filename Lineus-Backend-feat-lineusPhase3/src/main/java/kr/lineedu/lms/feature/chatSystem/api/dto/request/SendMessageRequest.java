package kr.lineedu.lms.feature.chatSystem.api.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SendMessageRequest {
    @NotNull(message = "Chat ID is required")
    @NotBlank(message = "Chat ID cannot be blank")
    private String chatId;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private String replyToMessageId;
    
    private String messageType = "text"; // text, image, video, system
}

