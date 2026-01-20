package kr.lineedu.lms.global.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomResponseWithPagination<T,P> {
    private int code;
    private String message;
    private T data;
    private P pagination;
}
