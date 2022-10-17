package com.playground.jeq.springtestapp.Controller;

import com.playground.jeq.springtestapp.Config.Utility.CommonUtil;
import com.playground.jeq.springtestapp.Model.API.Response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping()
    public BaseResponse<String> test(@RequestParam(name ="requestId", required = false, defaultValue = "") String requestId,
                                     HttpServletRequest request) {
        return new BaseResponse<>(
                CommonUtil.getRequestId(request),
                HttpStatus.OK.value(),
                "Test Response");
    }

}
