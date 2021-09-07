package org.zx.common.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zx.common.BaseResponse;
import org.zx.common.security.entity.CustomUserDetails;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@Slf4j
@RequestMapping("/api")
public class PingController extends BaseController{

    @GetMapping("/ping")
    public BaseResponse<Object> ping(){
        return success("pong");
    }

    @GetMapping("/info")
    public BaseResponse<Object> info(Principal principal, Authentication authentication){
        return success(authentication.getDetails());
    }
}
