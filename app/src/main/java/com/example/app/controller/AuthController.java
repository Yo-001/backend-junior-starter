package com.example.app.controller;

import com.example.app.dto.LoginRequest;
import com.example.app.dto.LoginResponse;
import com.example.app.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("Tentando autenticar: " + request.username() + "/" + request.password());
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            System.out.println("Autenticado: " + auth.isAuthenticated());

            String token = jwtUtil.generateToken(auth.getName());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (AuthenticationException ex) {
            System.out.println("Falha na autenticaçao: " + ex.getMessage());
            return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas"));
        }
    }
}
