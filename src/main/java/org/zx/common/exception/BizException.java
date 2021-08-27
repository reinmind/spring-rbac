package org.zx.common.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @author xiang.zhang
 */
public class BizException extends AuthenticationException {
    public BizException(String message) {
        super(message);
    }
}
