package com.emsi.eventplatform.eventservice.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalTokenFilter extends OncePerRequestFilter {

    private final String internalToken;
    private final String headerName;

    public InternalTokenFilter(
            @Value("${app.internal.token}") String internalToken,
            @Value("${app.internal.header}") String headerName
    ) {
        this.internalToken = internalToken;
        this.headerName = headerName;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/internal/")) {
            String token = request.getHeader(headerName);
            if (token == null || !token.equals(internalToken)) {
                response.setStatus(401);
                response.getWriter().write("Unauthorized internal call");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
