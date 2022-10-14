package com.playground.jeq.springtestapp.Config.Filter;

import com.playground.jeq.springtestapp.Config.Utility.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class RequestResponseLoggingFilter implements Filter {

    private final Logger LOGGER = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String log_message = new StringBuilder()
                .append("transaction request: ")
                .append("id: ").append(CommonUtil.getRequestId(request)).append(" | ")
                .append("method: ").append(request.getMethod()).append(" | ")
                .append("uri: ").append(request.getRequestURI()).toString();

        LOGGER.info("Initiating {}", log_message);
        chain.doFilter(request, response);
        LOGGER.info("Committing {}", log_message);
    }
}
