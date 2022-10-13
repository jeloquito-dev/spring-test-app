package com.playground.jeq.springtestapp.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidAccessTokenException extends Exception {

    public InvalidAccessTokenException(String message) {
        super(message);
    }
}
