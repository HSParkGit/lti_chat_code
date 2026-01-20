package kr.lineedu.lms.feature.chatSystem.api.feign;

import kr.lineedu.lms.config.constant.CanvasConstants;
import kr.lineedu.lms.config.feign.FeignBearerTokenConfiguration;
import kr.lineedu.lms.config.feign.FeignDateTimeFormatConfiguration;
import kr.lineedu.lms.feature.chatSystem.api.dto.feign.CanvasUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
    name = "canvasChatClient",
    url = "${util.canvas-url}/api/v1",
    configuration = {FeignBearerTokenConfiguration.class, FeignDateTimeFormatConfiguration.class}
)
public interface CanvasChatClient {

    @GetMapping(CanvasConstants.COURSE.SEARCH_COURSE_USERS)
    List<CanvasUserDto> searchCourseUsers(
        @PathVariable("course_id") Long courseId,
        @PathVariable("search_term") String searchTerm,
        @RequestParam(value = "as_user_id", required = false) Long asUserId
    );

    @GetMapping(CanvasConstants.USER.GET_USER_BY_ID)
    CanvasUserDto getUserById(
        @PathVariable("user_id") Long userId,
        @RequestParam(value = "as_user_id", required = false) Long asUserId
    );
}

