package org.mininuniver.interactiveMap.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_TIME_MS = 10 * 60 * 1000;

    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if ("/api/auth/login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String ip = request.getRemoteAddr();
            Attempt attempt = attempts.getOrDefault(ip, new Attempt(0, 0));
            long now = Instant.now().toEpochMilli();

            if (attempt.blockedUntil > now) {
                response.setStatus(429);
                response.getWriter().write("{\"error\":\"Too many login attempts. Try again later.\"}");
                return;
            }

            filterChain.doFilter(request, response);

            if (response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
                int newCount = attempt.count + 1;
                long blockedUntil = attempt.blockedUntil;
                if (newCount >= MAX_ATTEMPTS) {
                    blockedUntil = now + BLOCK_TIME_MS;
                    newCount = 0;
                }
                attempts.put(ip, new Attempt(newCount, blockedUntil));
            } else {
                attempts.remove(ip);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private static class Attempt {
        int count;
        long blockedUntil;
        Attempt(int count, long blockedUntil) {
            this.count = count;
            this.blockedUntil = blockedUntil;
        }
    }
}