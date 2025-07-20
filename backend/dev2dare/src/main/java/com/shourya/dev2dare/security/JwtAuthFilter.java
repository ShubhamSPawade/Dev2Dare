package com.shourya.dev2dare.security;

import com.shourya.dev2dare.model.College;
import com.shourya.dev2dare.model.Student;
import com.shourya.dev2dare.repository.CollegeRepository;
import com.shourya.dev2dare.repository.StudentRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.debug("[JwtAuthFilter] Filtering request: {} {}", request.getMethod(), request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(token);
                logger.debug("[JwtAuthFilter] Extracted email from token: {}", email);
            } catch (JwtException e) {
                logger.error("[JwtAuthFilter] Invalid JWT token: {}", e.getMessage());
                // Token invalid
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String role = jwtUtil.extractRole(token);
            logger.debug("[JwtAuthFilter] Extracted role from token: {}", role);

            if (jwtUtil.validateToken(token)) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role); // <-- important
                User user = new User(email, "", Collections.singleton(authority));
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, Collections.singleton(authority));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("[JwtAuthFilter] Authentication set for user: {} with role: {}", email, role);
            } else {
                logger.warn("[JwtAuthFilter] JWT token validation failed for user: {}", email);
            }
        }

        filterChain.doFilter(request, response);
        logger.debug("[JwtAuthFilter] Filter chain continued for request: {} {}", request.getMethod(), request.getRequestURI());
    }
}
