package org.zx.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zx.common.exception.BizException;
import org.zx.common.security.entity.User;
import org.zx.common.security.service.RedisService;
import org.zx.common.util.ApplicationContextUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.zx.common.security.JWTConst.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RedisService redisService;
    /**
     * 设置登录重定向
     *
     * @param authenticationManager
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        // 如果不设置这一行，spring会自动创建一个login的controller
        // 所以不用在controller当中设置login
        setFilterProcessesUrl(SIGN_UP_URL);
        setUserRepository(ApplicationContextUtil.context());
        setRedisService(ApplicationContextUtil.context());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            String username = user.getUsername();
            String password = user.getPassword();
            final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password, Lists.newArrayList());
            super.setDetails(request,authRequest);
            return authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            throw new BizException("登录请求格式错误");
        }
    }

    /**
     * 生成jwt Token并返回
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain chain
            , Authentication authResult) throws IOException {
        final String name = authResult.getName();
        String jwtToken = JWT.create()
                .withSubject(name)
                .withClaim("role",userRepository.findUserByUsername(name).getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes(StandardCharsets.UTF_8)));
        Cookie cookie = new Cookie("TOKEN", jwtToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        redisService.save(SessionToken.builder().
                name(name).token(jwtToken).build());
        response.getWriter().write(name + " success login");
        response.getWriter().flush();
    }

    public void setUserRepository(ApplicationContext ctx) {
        this.userRepository = ctx.getBean(UserRepository.class);
    }

    public void setRedisService(ApplicationContext ctx){
        this.redisService = ctx.getBean(RedisService.class);
    }
}
