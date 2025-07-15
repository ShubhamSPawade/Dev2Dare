package com.shourya.dev2dare.config;

import org.springframework.stereotype.Component;
import javax.servlet.*;
import java.io.IOException;

@Component
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Placeholder for JWT filter logic
        chain.doFilter(request, response);
    }
} 