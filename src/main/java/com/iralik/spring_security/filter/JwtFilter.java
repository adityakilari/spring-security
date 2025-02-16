package com.iralik.spring_security.filter;

import com.iralik.spring_security.serviceImp.Jwt;
import com.iralik.spring_security.serviceImp.UserDetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDED_PATHS = List.of("/login", "/register");
    private final Jwt jwt;
    private final UserDetail userDetail;

    @Autowired
    JwtFilter(Jwt jwt, UserDetail userDetail) {
        this.userDetail  = userDetail;
        this.jwt = jwt;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Skip filtering for login and register
        if (EXCLUDED_PATHS.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwt.getUserName(token);
        }

        UserDetails detail = userDetail.loadUserByUsername(username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwt.validateToken(token, detail)) {
                UsernamePasswordAuthenticationToken authToke =
                        new UsernamePasswordAuthenticationToken(detail, null, detail.getAuthorities());
                authToke.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToke);
            }
        }
        filterChain.doFilter(request, response);
    }
}
