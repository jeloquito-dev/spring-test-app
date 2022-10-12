package com.playground.jeq.springtestapp.Service;

import com.playground.jeq.springtestapp.Model.API.LoginResponse;
import com.playground.jeq.springtestapp.Model.UserRequest;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    LoginResponse authenticateUser(UserRequest userRequest);

}
