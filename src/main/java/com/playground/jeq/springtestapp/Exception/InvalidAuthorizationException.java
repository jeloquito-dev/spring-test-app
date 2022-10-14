package com.playground.jeq.springtestapp.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidAuthorizationException extends Exception {

    public InvalidAuthorizationException(String message) {
        super(message);
    }


}
