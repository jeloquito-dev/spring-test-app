package com.playground.jeq.springtestapp.Service;

import com.playground.jeq.springtestapp.Model.API.Response.AuthResponse;
import com.playground.jeq.springtestapp.Model.API.Request.UserRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AuthService {
    AuthResponse validateLogin(UserRequest userRequest);

    AuthResponse validateRefreshToken(HttpServletRequest request) throws Exception;
}
