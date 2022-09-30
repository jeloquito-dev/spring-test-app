package com.playground.jeq.springtestapp.Controller;

import com.playground.jeq.springtestapp.Model.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TestController {


    @GetMapping("/test")
    public BaseResponse<String> test(@RequestParam(name ="requestId", required = false, defaultValue = "") String requestId) {
        return new BaseResponse<>(
                ((requestId.equals("")) ? UUID.randomUUID().toString() : requestId),
                "Test Response");
    }

}
