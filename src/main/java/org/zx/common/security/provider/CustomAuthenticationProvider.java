package org.zx.common.security.provider;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.zx.common.exception.BizException;
import org.zx.common.security.entity.CustomUserDetails;
import org.zx.common.security.entity.User;
import org.zx.common.security.UserRepository;

import java.util.Collections;
import java.util.List;

/**
 * 登录控制 和ip控制
 * @author xiang.zhang
 * @date 2021/8/26
 */
@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    BCryptPasswordEncoder bCryptPasswordEncoder;
    UserRepository userRepository;
    List<String> allowedIps;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
        final WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        final String remoteAddress = details.getRemoteAddress();
        log.info("client address:{}",remoteAddress);
        String username = authentication.getName();
        String rawPasswd = authentication.getCredentials().toString();

        final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        final User userByUsername = userRepository.findUserByUsername(username);
        if(userByUsername == null){
            throw new BizException("用户不存在");
        }
        final boolean matches = bCryptPasswordEncoder.matches(rawPasswd, userByUsername.getPassword());
        if(matches) {
            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, rawPasswd, Collections.emptyList());
            return authenticationToken;
        }
        else{
            throw new BizException("密码错误");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    @Autowired
    public CustomAuthenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.allowedIps = Collections.singletonList("127.0.0.1");
    }
}
