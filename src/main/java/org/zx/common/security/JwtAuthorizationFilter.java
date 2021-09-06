package org.zx.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Lists;
import org.hibernate.Hibernate;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.zx.common.exception.BizException;
import org.zx.common.security.entity.Auth;
import org.zx.common.security.entity.CustomUserDetails;
import org.zx.common.security.entity.User;
import org.zx.common.security.service.RedisService;
import org.zx.common.util.ApplicationContextUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.zx.common.security.JWTConst.*;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;
    private RedisService redisService;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setUserRepository(ApplicationContextUtil.context());
        setRedisService(ApplicationContextUtil.context());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        // header不符合标准
        if(header == null || !header.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        if(authenticationToken != null){
            super.onSuccessfulAuthentication(request,response,authenticationToken);
        }
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request,response);
    }

    /**
     *  解析token信息
     * @param request
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        String token = request.getHeader(HEADER_STRING);

        if(token != null){
            try {
                DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                        .build()
                        .verify(token.replace(TOKEN_PREFIX, ""));
                String username = decodedJWT.getSubject();
                SessionToken sessionToken = redisService.get(username);
                // 校验redis当中缓存的session信息
                if(sessionToken == null || !(token.replace(TOKEN_PREFIX,"")).equals(sessionToken.getToken())){
                    throw new BizException("token 失效，请重新登录");
                }
                // 校验数据库中的用户信息
                User user = userRepository.findUserByUsername(username);
                String role = user.getRole();
                // TODO: lazy initialization bug
                final List<Auth> auths = user.getAuthorities();
                Hibernate.initialize(user.getAuthorities());
                List<SimpleGrantedAuthority> temp = new ArrayList<>();
                for(Auth auth:auths){
                    temp.add(new SimpleGrantedAuthority(auth.getAuthority()));
                }
                if(username != null){
                    final CustomUserDetails customUserDetails = new CustomUserDetails();

                    customUserDetails.setUsername(user.getUsername());
                    customUserDetails.setPassword(user.getPassword());
                    customUserDetails.setExpired(false);
                    customUserDetails.setEnable(user.isEnabled());
                    customUserDetails.setEmail(user.getEmail());
                    customUserDetails.setRole(user.getRole());

                    return new UsernamePasswordAuthenticationToken(username, null, temp);
                }
            }catch (RuntimeException exception){
                logger.info("token失效");
            }
            return null;
        }
        return null;
    }
    public void setUserRepository(ApplicationContext ctx) {
        this.userRepository = ctx.getBean(UserRepository.class);
    }
    public void setRedisService(ApplicationContext ctx){
        this.redisService = ctx.getBean(RedisService.class);
    }
}
