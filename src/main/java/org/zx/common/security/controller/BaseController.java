package org.zx.common.security.controller;

import org.springframework.http.HttpStatus;
import org.zx.common.BaseResponse;

public abstract class BaseController {



    protected <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(HttpStatus.OK.value(), "success", data);
    }

    protected <T> BaseResponse<T> fail(T data){
        return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "bad request", data);
    }
}
