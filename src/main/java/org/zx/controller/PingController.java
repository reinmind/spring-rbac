package org.zx.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PingController extends BaseController{

    @GetMapping("/ping")
    public Response<Object> ping(){
        return success("pong");
    }

    @GetMapping("/info")
    public Response<Object> info(@AuthenticationPrincipal Map<String,Object> userInfos){
        return success(userInfos);
    }
}
