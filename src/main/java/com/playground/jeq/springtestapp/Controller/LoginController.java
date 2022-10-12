package com.playground.jeq.springtestapp.Controller;

import com.playground.jeq.springtestapp.Model.UserRequest;
import com.playground.jeq.springtestapp.Service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity login(@RequestBody UserRequest request) {
        return ResponseEntity.ok().body(loginService.authenticateUser(request));
    }
}
