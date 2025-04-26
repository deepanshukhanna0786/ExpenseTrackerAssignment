package com.example.expensetrackerassignment.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/*")
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();
        String url = httpRequest.getRequestURL().toString();
        String userId = httpRequest.getUserPrincipal() != null ? httpRequest.getUserPrincipal().getName() : "Anonymous";

        logger.info("Request Method: {}, Request URL: {}, User: {}", method, url, userId);

        chain.doFilter(request, response);

        logger.info("Response Status: {}", httpResponse.getStatus());
    }
}
