package com.app.backend.services;

import com.app.backend.dto.LoginRequest;
import com.app.backend.dto.RegisterRequest;
import com.app.backend.models.User;
import com.app.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        authService = new AuthService(userRepository);
    }

    @Test
    void register_whenUsernameTaken_returnsEmpty() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("alice");
        req.setEmail("a@e.com");
        req.setPassword("pwd");

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(new User()));

        Optional<User> res = authService.register(req);

        assertTrue(res.isEmpty());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_success_savesUserWithEncodedPassword() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("bob");
        req.setEmail("b@e.com");
        req.setPassword("secret");

        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("b@e.com")).thenReturn(Optional.empty());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(captor.capture())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId("1");
            return u;
        });

        Optional<User> res = authService.register(req);

        assertTrue(res.isPresent());
        User saved = captor.getValue();
        assertEquals("bob", saved.getUsername());
        assertNotEquals("secret", saved.getPassword());
    }

    @Test
    void login_withCorrectCredentials_returnsUser() {
        String raw = "pw123";
        AuthService testService = new AuthService(userRepository);

        User stored = new User();
        stored.setId("u1");
        stored.setUsername("carol");
        stored.setEmail("c@e.com");
        stored.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(raw));

        when(userRepository.findByUsername("carol")).thenReturn(Optional.of(stored));

        LoginRequest req = new LoginRequest();
        req.setUsernameOrEmail("carol");
        req.setPassword(raw);

        Optional<User> out = testService.login(req);

        assertTrue(out.isPresent());
        assertEquals("carol", out.get().getUsername());
    }
}
