package com.geekstack.cards.filter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;
import java.io.IOException;

public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            logger.info("handling pre flight response");
            String origin = request.getHeader("Origin");

            String[] allowedOrigins = {
                    "http://localhost:4200",
                    "http://localhost:8080",
                    "https://localhost",
                    "https://geekstack.dev",
                    "https://cards.geekstack.dev"
            };

            for (String allowedOrigin : allowedOrigins) {
                if (allowedOrigin.equals(origin)) {
                    response.setHeader("Access-Control-Allow-Origin", allowedOrigin);
                    break;
                }
            }
            if (!response.containsHeader("Access-Control-Allow-Origin")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers",
                    "Authorization, Content-Type, X-Requested-With, Accept, Origin");
            response.setHeader("Access-Control-Allow-Credentials", "true");

            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        logger.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());

        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token)) {
            try {
                logger.info("Extracted Firebase Token: {}", token);
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

                logger.info("Successfully decoded Firebase token for UID: {}", decodedToken.getUid());

                Authentication authentication = new FirebaseAuthenticationToken(decodedToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Authentication set in SecurityContext for UID: {}", decodedToken.getUid());

            } catch (FirebaseAuthException e) {
                logger.error("Firebase Authentication Failed: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Invalid Firebase Token");
                return;
            }
        } else {
            logger.warn("No Authorization header or invalid format in request: {}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
