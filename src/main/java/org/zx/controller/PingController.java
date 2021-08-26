package org.zx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class PingController extends BaseController{

    @GetMapping("/ping")
    public Response<Object> ping(){
        return success("pong");
    }

    @GetMapping("/info")
    public Response<Object> info(Principal principal){
        return success(principal);
    }
}
