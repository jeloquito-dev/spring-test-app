package com.playground.jeq.springtestapp.Service;

import com.playground.jeq.springtestapp.Model.API.AuthResponse;
import com.playground.jeq.springtestapp.Model.UserRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AuthService {
    AuthResponse validateLogin(UserRequest userRequest);

    AuthResponse validateRefreshToken(HttpServletRequest request) throws Exception;
}
