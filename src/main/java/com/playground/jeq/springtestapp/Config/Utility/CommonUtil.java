package com.playground.jeq.springtestapp.Config.Utility;

import javax.servlet.http.HttpServletRequest;

import static com.playground.jeq.springtestapp.Config.Utility.StringReference.REQUEST_ID;

public class CommonUtil {

    public static String getRequestId(HttpServletRequest request) {
        return String.valueOf(request.getAttribute(REQUEST_ID));
    }
}
