package com.playground.jeq.springtestapp.Controller;

import com.playground.jeq.springtestapp.Config.Utility.CommonUtil;
import com.playground.jeq.springtestapp.Model.API.Response.AuthResponse;
import com.playground.jeq.springtestapp.Model.API.Response.BaseResponse;
import com.playground.jeq.springtestapp.Model.API.Request.UserRequest;
import com.playground.jeq.springtestapp.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.playground.jeq.springtestapp.Config.Utility.StringReference.*;


@RestController
@RequestMapping(AUTH_BASE)
public class AuthController {

    private final AuthService loginService;
    public AuthController(AuthService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(AUTH_LOGIN)
    public ResponseEntity<BaseResponse<AuthResponse>> login(HttpServletRequest request, @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(
                new BaseResponse<>(
                        CommonUtil.getRequestId(request),
                        HttpStatus.OK.value(),
                        loginService.validateLogin(userRequest)),
                HttpStatus.OK);
    }

    @PostMapping(AUTH_REFRESH)
    public ResponseEntity<BaseResponse<AuthResponse>> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ResponseEntity<>(
                new BaseResponse<>(
                        CommonUtil.getRequestId(request),
                        HttpStatus.OK.value(),
                        loginService.validateRefreshToken(request)),
                HttpStatus.OK);
    }

}