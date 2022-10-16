package com.playground.jeq.springtestapp.Config.Filter;

import com.playground.jeq.springtestapp.Config.Utility.CommonUtil;
import com.playground.jeq.springtestapp.Config.Security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.playground.jeq.springtestapp.Config.Utility.StringReference.*;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private TokenManager tokenManager;

    public JwtFilter(TokenManager tokenManager) {
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
                        CommonUtil.createForbiddenResponse(request, response, e);
                    }
                }
            }
            filterChain.doFilter(request, response);
        }

    }
}
