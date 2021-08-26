package org.zx.common.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zx.common.exception.BizException;
import org.zx.common.security.User;
import org.zx.common.security.UserRepository;

/**
 * @author xiang.zhang
 */
@Service
public class UserService {

    UserRepository userRepository;

    BCryptPasswordEncoder encoder;

    public User save(User user) {
        final boolean b = userRepository.existsByUsername(user.getUsername());
        if(b){
            throw new BizException("用户名已经存在");
        }
        final String raw = user.getPassword();
        final String encode = encoder.encode(raw);
        user.setPassword(encode);
        return userRepository.save(user);
    }

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }
}
