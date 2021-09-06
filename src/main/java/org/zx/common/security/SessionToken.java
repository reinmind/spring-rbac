package org.zx.common.security;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;



/**
 * @author xiang.zhang
 * @date 2021/9/6
 */
@Data
@Builder
@RedisHash("token")
public class SessionToken {
    @Id
    String name;

    String token;
}
