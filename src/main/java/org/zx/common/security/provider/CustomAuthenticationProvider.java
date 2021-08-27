package org.zx.common.security.provider;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zx.common.exception.BizException;
import org.zx.common.security.User;
import org.zx.common.security.UserRepository;

import java.util.Collections;

/**
 * @author xiang.zhang
 * @date 2021/8/26
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {
    BCryptPasswordEncoder bCryptPasswordEncoder;
    UserRepository userRepository;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
        String username = authentication.getName();
        String rawPasswd = authentication.getCredentials().toString();

        final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        final User userByUsername = userRepository.findUserByUsername(username);
        if(userByUsername == null){
            throw new BizException("用户不存在");
        }
        final boolean matches = bCryptPasswordEncoder.matches(rawPasswd, userByUsername.getPassword());
        if(matches) {
            return new UsernamePasswordAuthenticationToken(username, rawPasswd, Collections.emptyList());
        }
        else{
            throw new BizException("密码错误");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public CustomAuthenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }
}
