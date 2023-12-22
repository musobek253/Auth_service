package com.musobek.auth_service.config;

import com.musobek.auth_service.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal
            (@NonNull  HttpServletRequest request,
             @NonNull HttpServletResponse response,
             @NonNull FilterChain filterChain)
            throws ServletException, IOException
    {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final  String userEmail;
        if (authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail!=null&& SecurityContextHolder.getContext().getAuthentication() ==null){

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            var isValidToken = tokenRepository.findByToken(jwt)
                    .map(token -> !token.isExpired()&&!token.isRevoked()).orElse(false);
            if (jwtService.validToken(userDetails,jwt) && isValidToken){
                UsernamePasswordAuthenticationToken  authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
