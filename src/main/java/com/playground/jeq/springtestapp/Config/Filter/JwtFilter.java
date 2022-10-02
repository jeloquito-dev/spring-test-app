package com.playground.jeq.springtestapp.Config.Filter;

import com.playground.jeq.springtestapp.Config.Utility.JwtUserDetailsService;
import com.playground.jeq.springtestapp.Config.Utility.TokenManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtUserDetailsService jwtUserDetailsService;
    private TokenManager tokenManager;

    private final String HEADER = "Authorization";
    private final String BEARER = "Bearer ";

    public JwtFilter(JwtUserDetailsService jwtUserDetailsService, TokenManager tokenManager) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader(HEADER);
        String username = null;
        String token = null;

        if (tokenHeader != null && tokenHeader.startsWith(BEARER)) {
            token = tokenHeader.substring(BEARER.length());

            username = tokenManager.getUsernameFromToken(token);

            if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
                if (tokenManager.validateJwtToken(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
