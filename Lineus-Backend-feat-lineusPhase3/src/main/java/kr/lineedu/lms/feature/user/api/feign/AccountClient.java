package kr.lineedu.lms.feature.user.api.feign;

import kr.lineedu.lms.config.constant.CanvasConstants;
import kr.lineedu.lms.config.feign.FeignBearerTokenConfiguration;
import kr.lineedu.lms.config.feign.FeignDateTimeFormatConfiguration;
import kr.lineedu.lms.utils.dto.DivisionFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
    name = "accountClient",
    url = "${util.canvas-url}/api/v1",
    configuration = {FeignBearerTokenConfiguration.class, FeignDateTimeFormatConfiguration.class}
)
public interface AccountClient {

    @GetMapping(CanvasConstants.ACCOUNT.GET_DIVISION)
    List<DivisionFeignDto> getSubDivision(@PathVariable("account_id") Long accountId);
}

