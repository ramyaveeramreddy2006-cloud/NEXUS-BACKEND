package com.nexus.platform.service;

import com.nexus.platform.dto.AuthResponse;
import com.nexus.platform.dto.LoginRequest;
import com.nexus.platform.dto.RegisterRequest;
import com.nexus.platform.entity.Domain;
import com.nexus.platform.entity.User;
import com.nexus.platform.repository.UserRepository;
import com.nexus.platform.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must contain at least 6 characters.");
        }
        String email = request.getEmail().trim().toLowerCase(java.util.Locale.ROOT);
        if (users.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("An account with this email already exists. Sign in or use another email.");
        }
        Set<Domain> selectedDomains = new HashSet<>();
        if (request.getDomains() != null) selectedDomains.addAll(request.getDomains());
        selectedDomains.add(request.getDomain());

        User user = User.builder().name(request.getName().trim()).email(email)
                .password(encoder.encode(request.getPassword())).role(request.getRole())
                .domain(request.getDomain()).domains(selectedDomains).phone(request.getPhone()).city(request.getCity()).build();
        users.save(user);
        return response(user);
    }

    public AuthResponse login(LoginRequest request) {
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must contain at least 6 characters.");
        }
        User user = users.findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        return response(user);
    }

    private AuthResponse response(User user) {
        Set<Domain> selected = new HashSet<>();
        if (user.getDomains() != null) selected.addAll(user.getDomains());
        selected.add(user.getDomain());
        return new AuthResponse(jwt.generate(user.getEmail(), user.getRole().name()), user.getId(),
                user.getName(), user.getEmail(), user.getRole(), user.getDomain(), selected);
    }
}
