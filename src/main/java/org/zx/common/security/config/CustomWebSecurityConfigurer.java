package org.zx.common.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.zx.common.security.CustomAuthenticationEntryPoint;
import org.zx.common.security.JwtAuthenticationFilter;
import org.zx.common.security.JwtAuthorizationFilter;
import org.zx.common.security.UserRepository;
import org.zx.common.security.provider.CustomAuthenticationProvider;

import javax.sql.DataSource;

import static org.zx.common.security.JWTConst.SIGN_UP_URL;

@Component
@Slf4j
public class CustomWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    CustomAuthenticationEntryPoint authenticationEntryPoint;
    CustomAuthenticationProvider customAuthenticationProvider;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/info").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, SIGN_UP_URL,"/user/*").permitAll().anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(JdbcUserDetailsManager.DEF_AUTHORITIES_BY_USERNAME_QUERY);
//        jdbcUserDetailsManager.setGroupAuthoritiesSql(JdbcUserDetailsManager.DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY);
//        jdbcUserDetailsManager.setUsersByUsernameQuery(JdbcUserDetailsManager.DEF_USERS_BY_USERNAME_QUERY);
//        jdbcUserDetailsManager.setCreateUserSql(JdbcUserDetailsManager.DEF_CREATE_USER_SQL);
//        return jdbcUserDetailsManager;
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.customAuthenticationProvider);
    }

    @Autowired
    public CustomWebSecurityConfigurer(CustomAuthenticationEntryPoint authenticationEntryPoint,CustomAuthenticationProvider customAuthenticationProvider) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }
}
