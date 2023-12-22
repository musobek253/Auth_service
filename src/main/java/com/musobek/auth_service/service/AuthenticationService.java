package com.musobek.auth_service.service;

import com.musobek.auth_service.config.JwtService;
import com.musobek.auth_service.dto.AuthenticationResponse;
import com.musobek.auth_service.dto.RegisterRequest;
import com.musobek.auth_service.dto.authenticationRequest;
import com.musobek.auth_service.entity.Token;
import com.musobek.auth_service.entity.User;
import com.musobek.auth_service.entity.enam.Role;
import com.musobek.auth_service.entity.enam.TokenType;
import com.musobek.auth_service.repository.TokenRepository;
import com.musobek.auth_service.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.USER)
                .build();
        User saveUser = userRepository.save(user);
        var jwttoken = jwtService.generation(user);
        savedtoken(saveUser,jwttoken);
        return AuthenticationResponse.builder()
                .accessToken(jwttoken)
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
        var refreshtoken = jwtService.generateRefreshToken(user);
        savedtoken(user,token);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshtoken)
                .build();
    }
    private void savedtoken(User saveUser, String jwttoken) {
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
}
