package com.thecrowstudios.meowmarket.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class VisitTrackingInterceptor implements HandlerInterceptor {
    private final WebsiteMetricsService metricsService;
    
    @Autowired
    public VisitTrackingInterceptor(WebsiteMetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        metricsService.recordVisit(request);
        return true;
    }
}
