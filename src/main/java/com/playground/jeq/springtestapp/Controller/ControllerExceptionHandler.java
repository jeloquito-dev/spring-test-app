package com.playground.jeq.springtestapp.Controller;

import com.playground.jeq.springtestapp.Exception.InvalidAuthorizationException;
import com.playground.jeq.springtestapp.Exception.InvalidRefreshTokenException;
import com.playground.jeq.springtestapp.Model.API.Response.BaseResponse;
import com.playground.jeq.springtestapp.Model.API.Response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static com.playground.jeq.springtestapp.Config.Utility.StringReference.REQUEST_ID;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler({
            InvalidRefreshTokenException.class,
            InvalidAuthorizationException.class
    })
    public ResponseEntity<BaseResponse<ErrorResponse>> badRequestExceptionHandling(HttpServletRequest request, Exception ex) {

        String requestId = String.valueOf(request.getAttribute(REQUEST_ID));
        LOGGER.error("request id: {} : Error: {}", requestId, ex.getMessage());
        ex.printStackTrace();

        return new ResponseEntity<>(
                new BaseResponse<>(requestId,
                        HttpStatus.BAD_REQUEST.value(),
                        new ErrorResponse(ex.getMessage())), HttpStatus.BAD_REQUEST);
    }
}
