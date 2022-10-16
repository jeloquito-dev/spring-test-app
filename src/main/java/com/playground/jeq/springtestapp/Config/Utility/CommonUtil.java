package com.playground.jeq.springtestapp.Config.Utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.jeq.springtestapp.Model.API.Response.BaseResponse;
import com.playground.jeq.springtestapp.Model.API.Response.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.playground.jeq.springtestapp.Config.Utility.StringReference.REQUEST_ID;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CommonUtil {

    public static String getRequestId(HttpServletRequest request) {
        return String.valueOf(request.getAttribute(REQUEST_ID));
    }

    public static void createForbiddenResponse(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(
                response.getOutputStream(),
                new BaseResponse<>(
                        getRequestId(request),
                        FORBIDDEN.value(),
                        new ErrorResponse(e.getMessage())
                )
        );
    }
}
