package com.musobek.auth_service.controller;

import com.musobek.auth_service.dto.AuthenticationResponse;
import com.musobek.auth_service.dto.RegisterRequest;
import com.musobek.auth_service.dto.authenticationRequest;
import com.musobek.auth_service.entity.User;
import com.musobek.auth_service.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody authenticationRequest request){
        return ResponseEntity.ok(authenticationService.authentication(request));
    }
    @PostMapping("/refresh-token")
    public void refreshtoken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);

    }

    @GetMapping("/validateToken")
    public boolean validateToken(@RequestParam("token") String token){
        return authenticationService.validateToken(token);
    }
}
