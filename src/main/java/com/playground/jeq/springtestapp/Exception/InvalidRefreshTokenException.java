package com.playground.jeq.springtestapp.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRefreshTokenException extends Exception {

    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
