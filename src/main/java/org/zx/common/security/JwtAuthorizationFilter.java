package org.zx.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Lists;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.zx.common.util.ApplicationContextUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.zx.common.security.JWTConst.*;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setUserRepository(ApplicationContextUtil.context());
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
                final String role = userRepository.findUserByUsername(username).getRole();
                ArrayList<GrantedAuthority> es = Lists.newArrayList((GrantedAuthority) role::toString);
                if(username != null){

                    return new UsernamePasswordAuthenticationToken(username, null, es);
                }
            }catch (RuntimeException exception){
                logger.info("token time expire");
            }

            return null;
        }
        return null;
    }
    public void setUserRepository(ApplicationContext ctx) {
        this.userRepository = ctx.getBean(UserRepository.class);
    }
}
