package kr.lineedu.lms.global.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import kr.lineedu.lms.global.error.exception.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import kr.lineedu.lms.global.error.dto.ErrorCode;
import kr.lineedu.lms.global.error.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  

    /**
     * javax.validation.Valid 또는 @Validated binding error가 발생할 경우
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }

    /**
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }

    /**
     * 필수 요청 파라미터가 없거나 변환할 수 없는 경우 발생
     * (예: course_id 파라미터가 있지만 Long 타입으로 변환할 수 없는 경우)
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException e,
        HttpServletRequest request) {
        String parameterName = e.getParameterName();
        String parameterType = e.getParameterType();
        String requestPath = request.getRequestURI();
        
        String message = String.format(
            "Required request parameter '%s' (type: %s) is missing or cannot be converted. Path: %s",
            parameterName,
            parameterType,
            requestPath
        );
        
        log.warn("MissingServletRequestParameterException: {} - Path: {}", message, requestPath);
        
        String errorCode = ErrorCode.MISSING_REQUEST_PARAMETER_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.MISSING_REQUEST_PARAMETER_EXCEPTION.getHttpStatus();
        
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);
        return ResponseEntity.status(httpStatus)
            .body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) cause);
        }
        log.error("HttpMessageNotReadableException", ex);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), "Malformed JSON request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException ex) {
        String field = ex.getPath().get(0).getFieldName();
        String allowedValues = "";
        if (ex.getTargetType().isEnum()) {
            Object[] enumConstants = ex.getTargetType().getEnumConstants();
            allowedValues = " Allowed values: " + java.util.Arrays.toString(enumConstants);
        }
        String message = "Invalid value for field '" + field + "': " + ex.getValue() + "." + allowedValues;
        log.info("Invalid format: {}", message, ex);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    /**
     * 비즈니스 로직 실행 중 오류 발생
     */
    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException", e);

        String message = e.getMessage();
        String errorCode = e.getErrorCode();
        HttpStatus httpStatus = ErrorCode.BUSINESS_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(value = {DataNotFoundException.class})
    protected ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException e) {
        log.warn("DataNotFoundException", e);

        String message = e.getMessage();
        String errorCode = e.getErrorCode();
        HttpStatus httpStatus = ErrorCode.DATA_NOT_FOUND_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * 비즈니스 로직 실행 중 오류 발생
     */
    @ExceptionHandler(value = {FeignException.class})
    protected ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {
        log.error("FeignException", e);

        String message = ErrorCode.FEIGN_EXCEPTION.getMessage();
        String errorCode = ErrorCode.FEIGN_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.FEIGN_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * JWT 변조 발생
     */

    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<ErrorResponse> handleJwtSignatureException(SignatureException e) {
        log.error("JWT Signature Exception", e);

        String message = ErrorCode.INVALID_TOKEN.getMessage();
        String errorCode = ErrorCode.INVALID_TOKEN.getErrorCode();
        HttpStatus httpStatus = ErrorCode.INVALID_TOKEN.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * JWT 토큰으로 아무 값이나 사용하는경우
     */

    @ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<ErrorResponse> handleJwtMalformedException(MalformedJwtException e) {
        log.error("JWT Malformed Exception", e);

        String message = ErrorCode.INVALID_TOKEN.getMessage();
        String errorCode = ErrorCode.INVALID_TOKEN.getErrorCode();
        HttpStatus httpStatus = ErrorCode.INVALID_TOKEN.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * JWT 토큰 만료
     */

    @ExceptionHandler(value = ExpiredJwtException.class)
    protected ResponseEntity<ErrorResponse> handleJwtExpiredException(ExpiredJwtException e) {
        log.error("ExpiredJwtException", e);

        String message = ErrorCode.EXPIRED_JWT_EXCEPTION.getMessage();
        String errorCode = ErrorCode.EXPIRED_JWT_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.EXPIRED_JWT_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * JWT RefreshToken 만 들어온 경우
     */
    @ExceptionHandler(OnlyRefreshTokenException.class)
    protected ResponseEntity<ErrorResponse> handleOnlyRefreshToken(OnlyRefreshTokenException e) {
        log.error("OnlyRefreshTokenException", e);

        String message = ErrorCode.ONLY_REFRESH_TOKEN.getMessage();
        String errorCode = ErrorCode.ONLY_REFRESH_TOKEN.getErrorCode();
        HttpStatus httpStatus = ErrorCode.ONLY_REFRESH_TOKEN.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     *  JWT  accessToken 만료
     */
    @ExceptionHandler(TokenExpiredException.class)
    protected ResponseEntity<ErrorResponse> handleRefreshToken(TokenExpiredException e) {
        log.error("TokenExpiredException", e);

        String message = ErrorCode.EXPIRED_JWT_EXCEPTION.getMessage();
        String errorCode = ErrorCode.EXPIRED_JWT_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.EXPIRED_JWT_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorResponse> handleJwtException(JwtException e) {
        log.error("JWT Exception", e);

        String message = ErrorCode.INVALID_TOKEN.getMessage();
        String errorCode = ErrorCode.INVALID_TOKEN.getErrorCode();
        HttpStatus httpStatus = ErrorCode.INVALID_TOKEN.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(NoTokenException.class)
    protected ResponseEntity<ErrorResponse> handlerNoTokenException(NoTokenException e) {
        log.error("NoTokenException", e);

        String message = ErrorCode.NO_TOKEN_EXCEPTION.getMessage();
        String errorCode = ErrorCode.NO_TOKEN_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.NO_TOKEN_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * SAML 인증 Exception
     */
    @ExceptionHandler(SamlException.class)
    protected ResponseEntity<ErrorResponse> handleSamlException(SamlException e) {
        log.error("SamlException", e);

        String message = ErrorCode.SAML_EXCEPTION.getMessage();
        String errorCode = ErrorCode.SAML_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.SAML_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * SAML XML decode Exception
     */
    @ExceptionHandler(SamlXMLDecodeException.class)
    protected ResponseEntity<ErrorResponse> handleSamlXMLDecodeException(SamlXMLDecodeException e) {
        log.error("SamlXMLDecodeException", e);

        String message = ErrorCode.SAML_XML_DECODE_EXCEPTION.getMessage();
        String errorCode = ErrorCode.SAML_XML_DECODE_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.SAML_XML_DECODE_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * SAX Builder Exception
     */
    @ExceptionHandler(SAXBuilderException.class)
    protected ResponseEntity<ErrorResponse> handleSAXBuilderException(SAXBuilderException e) {
        log.error("SAXBuilderException", e);

        String message = ErrorCode.SAX_BUILDER_EXCEPTION.getMessage();
        String errorCode = ErrorCode.SAX_BUILDER_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.SAX_BUILDER_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * SAML XML resolver Exception
     */
    @ExceptionHandler(ResolveAttributeException.class)
    protected ResponseEntity<ErrorResponse> handleResolveAttributeException(ResolveAttributeException e) {
        log.error("ResolveAttributeException", e);

        String message = ErrorCode.RESOLVE_ATTRIBUTE_EXCEPTION.getMessage();
        String errorCode = ErrorCode.RESOLVE_ATTRIBUTE_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.RESOLVE_ATTRIBUTE_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * Inflater Exception
     */
    @ExceptionHandler(InflaterExeption.class)
    protected ResponseEntity<ErrorResponse> handleInflaterException(InflaterExeption e) {
        log.error("InflaterExeption", e);

        String message = ErrorCode.INFLATER_EXEPTION.getMessage();
        String errorCode = ErrorCode.INFLATER_EXEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.INFLATER_EXEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException", e);

        String message = ErrorCode.ACCESS_DENIED_EXCEPTION.getMessage();
        String errorCode = ErrorCode.ACCESS_DENIED_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.ACCESS_DENIED_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.error("BadCredentialsException", e);

        String errorCode = ErrorCode.NOT_FOUND_USER_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.NOT_FOUND_USER_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, e.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * User not found during JWT authentication
     * This can happen when a JWT token contains a loginId for a user that no longer exists or is inactive
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.warn("UsernameNotFoundException: {}", e.getMessage());

        String errorCode = ErrorCode.NOT_FOUND_USER_EXCEPTION.getErrorCode();
        HttpStatus httpStatus = ErrorCode.NOT_FOUND_USER_EXCEPTION.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, "User not found or inactive. Please login again.");

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * 인증이 필요한 리소스에 접근하려고 할 때 인증이 없거나 불충분한 경우
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleInsufficientAuthenticationException(
            InsufficientAuthenticationException e,
            HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        log.warn("InsufficientAuthenticationException: {} - Path: {}", e.getMessage(), requestPath);

        String message = ErrorCode.NO_TOKEN_EXCEPTION.getMessage();
        String errorCode = ErrorCode.NO_TOKEN_EXCEPTION.getErrorCode();
        // Return 401 Unauthorized for authentication failures instead of 200 OK
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        log.error("NullPointerException", e);

        String message = ErrorCode.INTERNAL_ERROR.getMessage();
        String errorCode = ErrorCode.INTERNAL_ERROR.getErrorCode();
        HttpStatus httpStatus = ErrorCode.INTERNAL_ERROR.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * 나머지 예외 발생
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception", e);

        String message = ErrorCode.INTERNAL_ERROR.getMessage();
        String errorCode = ErrorCode.INTERNAL_ERROR.getErrorCode();
        HttpStatus httpStatus = ErrorCode.INTERNAL_ERROR.getHttpStatus();

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
