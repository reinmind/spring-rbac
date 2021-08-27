package org.zx.common.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zx.common.BaseResponse;
import org.zx.common.exception.BizException;

/**
 * @author xiang.zhang
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerController extends BaseController {
    @ExceptionHandler({BizException.class, AuthenticationException.class})
    public BaseResponse<String> conflict(Exception e){
        return fail(e.getMessage());
    }
}
