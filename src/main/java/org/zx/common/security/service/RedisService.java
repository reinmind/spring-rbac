package org.zx.common.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zx.common.security.SessionToken;
import org.zx.common.security.SessionTokenRepo;

import java.util.Optional;

/**
 * @author xiang.zhang
 * @date 2021/9/6
 */
@Service
public class RedisService {
    SessionTokenRepo sessionTokenRepo;

    public void save(SessionToken token){
        sessionTokenRepo.save(token);
    }

    public void delete(String name){
        sessionTokenRepo.deleteById(name);
    }

    public SessionToken get(String name){
        Optional<SessionToken> byId = sessionTokenRepo.findById(name);
        return byId.orElse(null);
    }
    @Autowired
    public RedisService(SessionTokenRepo sessionTokenRepo) {
        this.sessionTokenRepo = sessionTokenRepo;
    }
}
