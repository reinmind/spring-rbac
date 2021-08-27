package org.zx.common.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zx.common.BaseResponse;
import org.zx.common.exception.BizException;

/**
 * @author xiang.zhang
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController extends BaseController {
    @ExceptionHandler(BizException.class)
    public BaseResponse<String> conflict(BizException e){
        return fail(e.getMessage());
    }
}
