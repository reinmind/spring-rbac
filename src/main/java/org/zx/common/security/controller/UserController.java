package org.zx.common.security.controller;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zx.common.BaseResponse;
import org.zx.common.security.User;
import org.zx.common.security.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author xiang.zhang
 */
@RestController
@RequestMapping("/user")
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
    public BaseResponse<Object> logout(@RequestHeader("Authorization")String token){
        userService.logout(token);
        return success("success");
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
