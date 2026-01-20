package kr.lineedu.lms.utils;

import kr.lineedu.lms.global.domain.CustomResponse;
import org.springframework.http.ResponseEntity;

public class ResponseUtil<T> {

    private int httpStatusCode;
    private String message;
    private T data;

    // Private constructor to enforce static instantiation
    private ResponseUtil() {}

    // Static factory method to get instance
    public static <T> ResponseUtil<T> getInstance() {
        return new ResponseUtil<>();
    }

    // Set http status code (chainable)
    public ResponseUtil<T> httpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
        return this;
    }

    // Set message (chainable)
    public ResponseUtil<T> message(String message) {
        this.message = message;
        return this;
    }

    // Set data (chainable)
    public ResponseUtil<T> data(T data) {
        this.data = data;
        return this;
    }

    // Build final ResponseEntity with ApiResponse wrapper
    public ResponseEntity<CustomResponse<T>> build() {
        CustomResponse<T> response = new CustomResponse<>();
        response.setCode(httpStatusCode);
        response.setMessage(message);
        response.setData(data);
        return ResponseEntity.status(httpStatusCode).body(response);
    }


}