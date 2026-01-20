package kr.lineedu.lms.utils;

import kr.lineedu.lms.feature.user.api.feign.AccountClient;
import kr.lineedu.lms.global.error.dto.ErrorCode;
import kr.lineedu.lms.global.error.exception.BusinessException;

public class DivisionUtil {

    public static String getNameBy(String name, AccountClient client) {
        return client.getSubDivision(1L)
            .stream()
            .filter(p -> p.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DIVISION))
            .getName();
    }
}

