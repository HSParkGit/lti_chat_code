package kr.lineedu.lms.global.error.exception;

import kr.lineedu.lms.global.error.dto.ErrorCode;
import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException {
    private final String errorCode;

    public DataNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getErrorCode();
    }

    public DataNotFoundException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode.getErrorCode();
    }

}
