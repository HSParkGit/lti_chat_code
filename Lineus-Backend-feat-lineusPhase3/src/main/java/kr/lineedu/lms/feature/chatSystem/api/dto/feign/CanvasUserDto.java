package kr.lineedu.lms.feature.chatSystem.api.dto.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CanvasUserDto {
    private Long id;
    private String name;
    
    @JsonProperty("login_id")
    private String loginId;
    
    private String email;
    
    @JsonProperty("avatar_url")
    private String avatarUrl;
    
    @JsonProperty("short_name")
    private String shortName;
}

