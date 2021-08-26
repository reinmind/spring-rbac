package org.zx.common.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.zx.common.security.JwtAuthenticationFilter;
import org.zx.common.security.JwtAuthorizationFilter;
import org.zx.common.security.UserRepository;
import org.zx.common.security.provider.CustomAuthenticationProvider;

import javax.sql.DataSource;

import static org.zx.common.security.JWTConst.SIGN_UP_URL;

@Component
public class CustomWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CustomWebSecurityConfigurer.class);
    BCryptPasswordEncoder encoder;
    UserRepository userRepository;
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/info").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, SIGN_UP_URL,"/user/*").permitAll().anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
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
        auth.authenticationProvider(new CustomAuthenticationProvider(encoder,userRepository));
    }

    @Autowired
    public CustomWebSecurityConfigurer(BCryptPasswordEncoder encoder, UserRepository userRepository, DataSource dataSource) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.dataSource = dataSource;
    }
}
