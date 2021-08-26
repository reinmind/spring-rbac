package org.zx.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.zx.common.security.JwtAuthenticationFilter;
import org.zx.common.security.JwtAuthorizationFilter;
import org.zx.common.security.provider.CustomAuthenticationProvider;

import javax.sql.DataSource;

import static org.zx.common.security.code.JWTConst.SIGN_UP_URL;

@Configuration
public class CustomWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(CustomWebSecurityConfigurer.class);

    @Autowired
    DataSource dataSource;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll().anyRequest().authenticated()
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
        auth.authenticationProvider(new CustomAuthenticationProvider());
    }
}
