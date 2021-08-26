package org.zx.common.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zx.common.BaseResponse;

import java.security.Principal;

@RestController
public class PingController extends BaseController{

    @GetMapping("/ping")
    public BaseResponse<Object> ping(){
        return success("pong");
    }

    @GetMapping("/info")
    public BaseResponse<Object> info(Principal principal){
        return success(principal);
    }
}