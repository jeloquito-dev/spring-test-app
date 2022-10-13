package com.playground.jeq.springtestapp.Config.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.jeq.springtestapp.Config.Utility.CommonUtil;
import com.playground.jeq.springtestapp.Config.Utility.TokenManager;
import com.playground.jeq.springtestapp.Model.API.BaseResponse;
import com.playground.jeq.springtestapp.Model.API.ErrorResponse;
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
import java.util.UUID;

import static com.playground.jeq.springtestapp.Config.Utility.StringReference.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CustomJwtFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;
    private TokenManager tokenManager;

    public CustomJwtFilter(UserDetailsService userDetailsService, TokenManager tokenManager) {
        this.userDetailsService = userDetailsService;
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        request.setAttribute(REQUEST_ID, UUID.randomUUID().toString());

        if(request.getServletPath().equals(AUTH_BASE + AUTH_LOGIN)
                || request.getServletPath().equals(AUTH_BASE + AUTH_REFRESH)) {
            filterChain.doFilter(request, response);
        } else {
            String token = tokenManager.identifyToken(request);

            if (null != token) {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        UsernamePasswordAuthenticationToken authenticationToken = tokenManager.getAuthenticationToken(token);
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } catch (Exception e) {
                        response.setStatus(FORBIDDEN.value());
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(
                                response.getOutputStream(),
                                new BaseResponse<>(
                                        CommonUtil.getRequestId(request),
                                        FORBIDDEN.value(),
                                        new ErrorResponse(e.getMessage())
                                )
                        );
                    }
                }
            }

            filterChain.doFilter(request, response);
        }
    }
}
