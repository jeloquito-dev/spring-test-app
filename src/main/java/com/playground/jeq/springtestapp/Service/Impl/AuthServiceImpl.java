package com.playground.jeq.springtestapp.Service.Impl;

import com.playground.jeq.springtestapp.Config.Utility.TokenManager;
import com.playground.jeq.springtestapp.Exception.InvalidAuthorizationException;
import com.playground.jeq.springtestapp.Exception.InvalidRefreshTokenException;
import com.playground.jeq.springtestapp.Model.API.AuthResponse;
import com.playground.jeq.springtestapp.Model.UserRequest;
import com.playground.jeq.springtestapp.Service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Map;
import java.util.ResourceBundle;

import static com.playground.jeq.springtestapp.Config.Utility.StringReference.*;

@Service
public class AuthServiceImpl implements AuthService {

    private final ResourceBundle messages = ResourceBundle.getBundle("messages");
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
     * @param userRequest       Object containing the credentials
     * @return AuthResponse     Object containing the generated access and refresh token
     */
    @Override
    public AuthResponse validateLogin(UserRequest userRequest) {
        return getTokens(
                authenticateUser(userRequest)
        );
    }

    /**
     * Validates the refresh token and generate new tokens.
     * Identifies the token and validates if the token is a refresh token using the Token Manager.
     * @param request           The HTTPServletRequest Object with the authorization information
     * @return AuthResponse     Object containing the generated access and refresh token
     * @throws Exception        Throws InvalidRefreshTokenException if the refresh token is invalid
     *                          or InvalidAuthorizationException if the token header is not correct or invalid
     */
    @Override
    public AuthResponse validateRefreshToken(HttpServletRequest request) throws Exception {

        String token = tokenManager.identifyToken(request);

        if (null != token) {
            if (tokenManager.validateRefreshToken(token)) {
                String username = tokenManager.getUsernameFromToken(token);
                return getTokens(username);
            } else {
                throw new InvalidRefreshTokenException(messages.getString("Invalid.Refresh.Token"));
            }
        } else {
            throw new InvalidAuthorizationException(messages.getString("Invalid.Authorization.Header"));
        }

    }

    /**
     * Authenticates user via Authentication Manager.
     * The user request password is decoded using Base64 decoder.
     * This method throws an exception if the credentials are incorrect.
     * @param userRequest       UserRequest Object containing the username and password for authentication
     * @return username         Returns the username if the authentication is valid
     */
    private String authenticateUser(UserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = decodeBase64Password(userRequest.getPassword());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return username;
    }

    /**
     * Gets token via Token Manager's generateToken method.
     * This method gets the user details and submits it as parameter to the Token Manager.
     * @param username          username of the app user
     * @return AuthResponse     Object containing the generated access and refresh token
     */
    private AuthResponse getTokens(String username) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Map<String, String> tokens = tokenManager.generateToken(userDetails);

        return new AuthResponse(
                tokens.get(ACCESS_TOKEN),
                tokens.get(REFRESH_TOKEN));
    }

    /**
     * Decodes password using Base64
     * @param password      The encoded password of the user
     * @return              decoded password
     */
    private String decodeBase64Password(String password) {
        return new String(Base64.getDecoder().decode(password.getBytes()));
    }
}
