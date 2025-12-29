package com.emsi.eventplatform.paymentservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final String secret;

    public JwtAuthFilter(@Value("${app.jwt.secret}") String secret) {
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String userId = claims.getSubject();
                Object rolesObj = claims.get("roles");
                List<String> roles = (rolesObj instanceof List<?> l)
                        ? l.stream().map(String::valueOf).toList()
                        : List.of();

                var authToken = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).toList()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (Exception ignored) {
                // token invalide => pas d'auth
            }
        }

        filterChain.doFilter(request, response);
    }
}
