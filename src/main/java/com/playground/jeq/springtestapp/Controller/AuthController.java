package com.playground.jeq.springtestapp.Controller;

import com.playground.jeq.springtestapp.Model.API.AuthResponse;
import com.playground.jeq.springtestapp.Model.UserRequest;
import com.playground.jeq.springtestapp.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthService loginService;

    public AuthController(AuthService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok().body(loginService.validateLogin(userRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok().body(loginService.validateRefreshToken(request));
    }

}