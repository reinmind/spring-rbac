package org.zx.common.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zx.common.BaseResponse;
import org.zx.common.exception.BizException;
import org.zx.common.util.ApplicationContextUtil;

import javax.transaction.TransactionManager;

/**
 * @author xiang.zhang
 * @date 2021/8/30
 */
@RestController
@Slf4j
@RequestMapping("/log")
public class LogController extends BaseController{
    ApplicationContextUtil ctx;
    @GetMapping("/test")
    public BaseResponse<String> log(){
        log.info("info");
        log.trace("trace");
        log.error("error");
        log.debug("debug");
        log.warn("warn");
        return success("logged");
    }
    @GetMapping("/bean")
    public BaseResponse<Object> log(@RequestParam("name") String name){
        ApplicationContext context = ApplicationContextUtil.context();
        try {
            Object bean = context.getBean(name);
            return success(bean.getClass());
        }catch(RuntimeException e){
            throw new BizException(e.getMessage());
        }
    }

    @Autowired
    public LogController(ApplicationContextUtil ctx) {
        this.ctx = ctx;
    }
}
