package org.zx.common.security.provider;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;

/**
 * @author xiang.zhang
 * @date 2021/8/26
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String passwd = authentication.getCredentials().toString();
        if("foo".equals(username) && "bar".equals(passwd)) {
            return new UsernamePasswordAuthenticationToken(username, passwd, Collections.emptyList());
        }
        else{
            throw new RuntimeException("External system authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
