package org.zx.common.security.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author xiang.zhang
 */
public class CustomAuthority implements GrantedAuthority {
    String authority;
    @Override
    public String getAuthority() {
        return authority;
    }

    public CustomAuthority(String authority) {
        this.authority = authority;
    }
}
