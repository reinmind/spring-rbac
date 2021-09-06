package org.zx.common.security.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zx.common.security.UserRepository;
import org.zx.common.security.entity.CustomUserDetails;
import org.zx.common.security.entity.User;

/**
 * @author xiang.zhang
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails customUserDetails = new CustomUserDetails();
        final User userByUsername = userRepository.findUserByUsername(username);
        BeanUtils.copyProperties(userByUsername,customUserDetails);
        return customUserDetails;
    }

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
