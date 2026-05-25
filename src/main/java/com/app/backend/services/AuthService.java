package com.app.backend.services;

import com.app.backend.dto.LoginRequest;
import com.app.backend.dto.RegisterRequest;
import com.app.backend.models.User;
import com.app.backend.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> register(RegisterRequest req) {
        // check existing username/email
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            return Optional.empty();
        }
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return Optional.empty();
        }

        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));

        User saved = userRepository.save(u);
        saved.setPassword(null);
        return Optional.of(saved);
    }

    public Optional<User> login(LoginRequest req) {
        Optional<User> maybe = userRepository.findByUsername(req.getUsernameOrEmail());
        if (maybe.isEmpty()) {
            maybe = userRepository.findByEmail(req.getUsernameOrEmail());
        }
        if (maybe.isEmpty()) return Optional.empty();

        User u = maybe.get();
        if (passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            u.setPassword(null);
            return Optional.of(u);
        }
        return Optional.empty();
    }
}
