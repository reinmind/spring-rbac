package org.zx.common.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zx.common.BaseResponse;
import org.zx.common.security.entity.User;
import org.zx.common.security.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiang.zhang
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {
    UserService userService;

    @PostMapping("/create")
    public BaseResponse<User> create(@RequestBody User user){
        final User save = userService.save(user);
        return success(save);
    }

    @GetMapping("/delete")
    public BaseResponse<Object> delete(@RequestParam("username") String username){

        return success(userService.delete(username));
    }

    @GetMapping("/logout")
    public BaseResponse<Object> logout(HttpServletRequest request,HttpServletResponse response){
        // invalid cookie
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies){
            cookie.setMaxAge(0);
            String token = cookie.getValue();
            userService.logout(token);
            response.addCookie(cookie);
        }
        return success("success");
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
