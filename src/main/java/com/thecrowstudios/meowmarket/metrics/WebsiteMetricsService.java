package com.thecrowstudios.meowmarket.metrics;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class WebsiteMetricsService {
    private final VisitLogRepository visitLogRepository;

    private final MeterRegistry meterRegistry;

    private Set<String> knownBotPatterns = new HashSet<>();

    @Autowired
    public WebsiteMetricsService(VisitLogRepository visitLogRepository, MeterRegistry meterRegistry) {
        this.visitLogRepository = visitLogRepository;
        this.meterRegistry = meterRegistry;

        initalizeBotPatterns();
    }

    private void initalizeBotPatterns() {
        this.knownBotPatterns = new HashSet<>(
                Arrays.asList("bot", "crawler", "spider", "slurp", "bingbot", "googlebot", "yandexbot", "baidespider",
                        "semrushbot", "ahrefsbot", "msnbot", "facebookexternalhit", "twitterbot"));
    }

    public boolean isBot(String userAgent) {
        if (userAgent.isBlank())
            return true;

        String normalizedUserAgent = userAgent.toLowerCase();

        for (String pattern : knownBotPatterns) {
            if (normalizedUserAgent.contains(pattern))
                return true;
        }

        if (normalizedUserAgent.contains("headless") ||
                normalizedUserAgent.contains("phantomjs") ||
                normalizedUserAgent.contains("selenium")) {
            return true;
        }

        if (!normalizedUserAgent.contains("mozilla") &&
                !normalizedUserAgent.contains("chrome") &&
                !normalizedUserAgent.contains("safari") &&
                !normalizedUserAgent.contains("firefox") &&
                !normalizedUserAgent.contains("edge")) {
            return true;
        }

        return false;
    }

    public void recordVisit(HttpServletRequest request) {
        String userAgent = request.getHeader("User-agent");
        String ipAddress = getClientIpAddress(request);
        boolean isBot = isBot(userAgent);

        VisitLog visitLog = new VisitLog();
        visitLog.setIpAddress(ipAddress);
        visitLog.setUserAgent(userAgent);
        visitLog.setTimestamp(LocalDateTime.now());
        visitLog.setIsBot(isBot);
        visitLog.setSessionId(request.getSession().getId());
        visitLog.setPath(request.getRequestURI());
        visitLog.setMethod(request.getMethod());

        visitLogRepository.save(visitLog);

        if (isBot)
            meterRegistry.counter("website_bot_visits").increment();
        else
            meterRegistry.counter("website_human_visits").increment();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For"); // de-facto standard header for identifying the
                                                                     // originating IP address of a client connecting to
                                                                     // a web server through a proxy server
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    public Map<String, Long> getVisitStats(LocalDateTime start, LocalDateTime end) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalVisits", visitLogRepository.count());
        stats.put("botVisits", visitLogRepository.countByIsBotAndTimestampBetween(true, start, end));
        stats.put("humanVisits", visitLogRepository.countByIsBotAndTimestampBetween(false, start, end));
        return stats;
    }
}