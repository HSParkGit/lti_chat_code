package kr.lineedu.lms.global.controller;

import kr.lineedu.lms.global.domain.CustomAdminResponse;
import kr.lineedu.lms.global.domain.CustomResponse;
import kr.lineedu.lms.global.domain.CustomResponseWithPagination;
import org.springframework.http.HttpStatus;

public class BaseController {

    public <T> CustomResponse<T> createHttpResponse(HttpStatus httpStatus, String message, T data){
        return new CustomResponse<T>(httpStatus.value(), message, data);
    }

    public <T,D> CustomAdminResponse<T,D> createAdminHttpResponse(HttpStatus httpStatus, String message, T admin , D data){
        return new CustomAdminResponse<>(httpStatus.value(),message,admin, data);
    }

    public <T,P> CustomResponseWithPagination<T,P> createHttpResponse(HttpStatus httpStatus, String message, T data, P pagination){
        return new CustomResponseWithPagination<>(httpStatus.value(), message, data,pagination);
    }
}