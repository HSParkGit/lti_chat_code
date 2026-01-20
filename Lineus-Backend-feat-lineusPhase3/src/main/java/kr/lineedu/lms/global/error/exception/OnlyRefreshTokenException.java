package kr.lineedu.lms.global.error.exception;

public class OnlyRefreshTokenException extends RuntimeException{
    public OnlyRefreshTokenException(String msg) {
        super(msg);
    }

}
