package com.playground.jeq.springtestapp.Model;

import java.io.Serializable;

public class UserResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String token;

    public UserResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}