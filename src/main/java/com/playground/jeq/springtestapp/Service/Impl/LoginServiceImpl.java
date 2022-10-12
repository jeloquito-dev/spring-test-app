package com.playground.jeq.springtestapp.Service.Impl;

import com.playground.jeq.springtestapp.Config.Utility.TokenManager;
import com.playground.jeq.springtestapp.Model.API.LoginResponse;
import com.playground.jeq.springtestapp.Model.UserRequest;
import com.playground.jeq.springtestapp.Service.LoginService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;

    public LoginServiceImpl(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, TokenManager tokenManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
    }

    @Override
    public LoginResponse authenticateUser(UserRequest userRequest) {

        String username = userRequest.getUsername();
        String password = userRequest.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Map<String, String> tokens = tokenManager.generateToken(userDetails);

        return new LoginResponse(
                tokens.get("access_token"),
                tokens.get("refresh_token"));

    }
}
