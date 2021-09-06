package org.zx.common.security.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

/**
 * @author xiang.zhang
 */
@Getter
@Setter
@Embeddable
public class Auth {
    String authority;
}
