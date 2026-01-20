package kr.lineedu.lms.global.error.dto;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private final String errorCode;
    private final String errorMessage;

    public static ErrorResponse of(final String errorCode, final String errorMessage) {
        return ErrorResponse.builder()
            .errorCode(errorCode)
            .errorMessage(errorMessage)
            .build();
    }

    public static ErrorResponse of(final String errorCode, final BindingResult bindingResult) {
        return ErrorResponse.builder()
            .errorCode(errorCode)
            .errorMessage(createErrorMesage(bindingResult))
            .build();
    }

    private static String createErrorMesage(final BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            if (!isFirst) {
                sb.append(", ");
            } else {
                isFirst = false;
            }

            sb.append("[");
            sb.append(fieldError.getField());
            sb.append("]");
            sb.append(fieldError.getDefaultMessage());
        }

        return sb.toString();
    }
}
