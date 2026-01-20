package kr.lineedu.lms.global.error.exception;

import kr.lineedu.lms.global.error.dto.ErrorCode;

public class NoTokenException extends RuntimeException {

    private final ErrorCode errorCode;

    public NoTokenException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
