package com.playground.jeq.springtestapp.Service.Impl;

import com.playground.jeq.springtestapp.Config.Utility.TokenManager;
import com.playground.jeq.springtestapp.Model.API.AuthResponse;
import com.playground.jeq.springtestapp.Model.UserRequest;
import com.playground.jeq.springtestapp.Service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;

    public AuthServiceImpl(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, TokenManager tokenManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }

    /**
     * Processes the authentication of user
     * @param userRequest -> user credentials
     * @return AuthResponse -> tokens (access & refresh token)
     */
    @Override
    public AuthResponse validateLogin(UserRequest userRequest) {
        return getTokens(
                authenticateUser(userRequest)
        );
    }

    @Override
    public AuthResponse validateRefreshToken(HttpServletRequest request) throws Exception {

        String token = tokenManager.identifyToken(request);
        if (null != token) {
            if (tokenManager.validateRefreshToken(token)) {
                String username = tokenManager.getUsernameFromToken(token);
                return getTokens(username);
            }
        }
        throw new Exception("Invalid refresh token");
    }

    private String authenticateUser(UserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return username;
    }

    private AuthResponse getTokens(String username) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        System.out.println(username);
        Map<String, String> tokens = tokenManager.generateToken(userDetails);

        return new AuthResponse(
                tokens.get("access_token"),
                tokens.get("refresh_token"));
    }


}
