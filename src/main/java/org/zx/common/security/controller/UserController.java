package org.zx.common.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zx.common.BaseResponse;
import org.zx.common.security.User;
import org.zx.common.security.service.UserService;

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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
