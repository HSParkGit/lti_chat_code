package kr.lineedu.lms.feature.chatSystem.api.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateGroupChatRequest {
    @NotBlank(message = "Group name is required")
    private String name;
    
    @NotNull(message = "User IDs are required")
    @Size(min = 1, message = "At least one user ID is required")
    private List<Long> userIds;
}

