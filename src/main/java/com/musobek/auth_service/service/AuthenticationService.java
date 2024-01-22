package com.musobek.auth_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musobek.auth_service.config.JwtService;
import com.musobek.auth_service.dto.AuthenticationResponse;
import com.musobek.auth_service.dto.RegisterRequest;
import com.musobek.auth_service.dto.authenticationRequest;
import com.musobek.auth_service.entity.Token;
import com.musobek.auth_service.entity.User;
import com.musobek.auth_service.entity.enam.TokenType;
import com.musobek.auth_service.repository.TokenRepository;
import com.musobek.auth_service.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            return  AuthenticationResponse.builder()
                    .msg("Already exist By email")
                    .build();
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())){
            return AuthenticationResponse.builder()
                    .msg("Already exist by phone number")
                    .build();
        }
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .build();
        User saveUser = userRepository.save(user);
        var jwtToken = jwtService.generation(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveinToken(saveUser,jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authentication(authenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var token = jwtService.generation(user);
        revokedToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveinToken(user,token);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
    private void saveinToken(User saveUser, String jwttoken) {
        var token = Token.builder()
                .user(saveUser)
                .token(jwttoken)
                .expired(false)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    private void revokedToken(User user){
        var validUsersTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUsersTokens.isEmpty()){
            return;
        }
        validUsersTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUsersTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final  String userEmail;
        if (authHeader==null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken).toLowerCase();
        if (userEmail!=null){
            var user = userRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.validToken(user,refreshToken)){
                var accessToken = jwtService.generation(user);
                revokedToken(user);
                saveinToken(user,accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }
        }
    }

    public List<User> getallusers() {
        return userRepository.findAll();
    }

    public boolean validateToken(String token) {
       return jwtService.validateToken(token);
    }
}
