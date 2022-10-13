package com.playground.jeq.springtestapp.Config.Security;

import com.playground.jeq.springtestapp.Config.Filter.CustomJwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;

@Configuration
public class WebSecurityConfiguration {

    private UserDetailsService userDetailsService;
    private CustomJwtFilter customJwtFilter;

    public WebSecurityConfiguration(UserDetailsService userDetailsService, CustomJwtFilter customJwtFilter) {
        this.userDetailsService = userDetailsService;
        this.customJwtFilter = customJwtFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/api/auth/**").permitAll();
        http.authorizeRequests().antMatchers(GET,"/api/user/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/test/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
