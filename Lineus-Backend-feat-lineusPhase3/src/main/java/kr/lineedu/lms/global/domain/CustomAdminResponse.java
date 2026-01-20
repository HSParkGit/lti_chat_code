package kr.lineedu.lms.global.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomAdminResponse<T,D> {

    private int code;
    private String message;
    private T admin;
    private D data;

}
