package org.zx.common.security.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zx.common.BaseResponse;
import org.zx.common.security.entity.User;
import org.zx.common.security.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xiang.zhang
 */
@RestController
@RequestMapping("/api")
public class UserController extends BaseController {
    UserService userService;

    @PostMapping("/user/create")
    public BaseResponse<User> create(@RequestBody User user){
        final User save = userService.save(user);
        return success(save);
    }

    @GetMapping("/user/delete")
    public BaseResponse<Object> delete(@RequestParam("username") String username){

        return success(userService.delete(username));
    }

    @GetMapping("/logout")
    public BaseResponse<Object> logout(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies){
            final String name = cookie.getName();
            if("TOKEN".equals(name)){
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                userService.invalidCache(cookie.getValue());
            }
        }

        return success("success");
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
