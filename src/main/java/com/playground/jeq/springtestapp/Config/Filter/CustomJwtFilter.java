package com.playground.jeq.springtestapp.Config.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.jeq.springtestapp.Config.Utility.TokenManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CustomJwtFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;
    private TokenManager tokenManager;

    private final String BEARER = "Bearer ";

    public CustomJwtFilter(UserDetailsService userDetailsService, TokenManager tokenManager) {
        this.userDetailsService = userDetailsService;
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().equals("/api/login")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
                token = authorizationHeader.substring(BEARER.length());

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        UsernamePasswordAuthenticationToken authenticationToken = tokenManager.getAuthenticationToken(token);

                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } catch (Exception e) {
                        response.setStatus(FORBIDDEN.value());
                        response.setContentType(APPLICATION_JSON_VALUE);
                        Map<String, String> error = new HashMap<>();
                        error.put("error_message", e.getMessage());
                        new ObjectMapper().writeValue(response.getOutputStream(), error);
                    }

                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
