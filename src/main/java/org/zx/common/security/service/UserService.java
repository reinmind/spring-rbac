package org.zx.common.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zx.common.exception.BizException;
import org.zx.common.security.entity.User;
import org.zx.common.security.UserRepository;
import javax.transaction.Transactional;
import java.util.Map;
import static org.zx.common.security.JWTConst.SECRET;

/**
 * @author xiang.zhang
 */
@Service
@Transactional(rollbackOn = RuntimeException.class)
public class UserService {
    Map<String,Object> stringObjectMapper;

    UserRepository userRepository;

    BCryptPasswordEncoder encoder;

    RedisService redisService;

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

    public Map<String,Object> delete(String username){
        int b = userRepository.deleteByUsername(username);
        stringObjectMapper.put(username,"false");
        if(b > 0){
            throw new BizException("删除成功，正在rollback");
        }
        return stringObjectMapper;
    }

    public void invalidCache(String token){
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token);
        String username = decodedJWT.getSubject();
        // invalid redis
        this.redisService.delete(username);
    }

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder,RedisService redisService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.stringObjectMapper = Maps.newHashMap();
        this.redisService = redisService;
    }

}
