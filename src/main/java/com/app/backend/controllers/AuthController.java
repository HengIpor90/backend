package com.app.backend.controllers;

import com.app.backend.dto.AuthResponse;
import com.app.backend.dto.LoginRequest;
import com.app.backend.dto.RegisterRequest;
import com.app.backend.models.User;
import com.app.backend.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        Optional<User> saved = authService.register(req);
        if (saved.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse("Username or email already exists", null));
        }
        User u = saved.get();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse("User registered", u.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<User> u = authService.login(req);
        if (u.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Invalid credentials", null));
        }
        return ResponseEntity.ok(new AuthResponse("Login successful", u.get().getUsername()));
    }
}
