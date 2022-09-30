package com.playground.jeq.springtestapp.Config.Filter;

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
        LOGGER.info("Starting a transaction for req : {}", request.getMethod() + " : " + request.getRequestURI());

        chain.doFilter(request, response);

        LOGGER.info("Committing a transaction for req : {}", request.getMethod() + " : " +  request.getRequestURI());
    }
}
