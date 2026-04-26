package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.auth.ForgotPasswordRequest;
import com.example.demo.dto.auth.ResetPasswordRequest;
import com.example.demo.dto.auth.SetRoleRequest;
import com.example.demo.dto.auth.SocialAuthRequest;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private static final Set<String> VALID_ROLES = Set.of("DOCTOR", "NURSE", "PATIENT");

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            // fallback for legacy plain-text passwords in existing DB
            User legacy = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new BadRequestException("Invalid username or password"));
            if (!legacy.getPassword().equals(request.getPassword())) {
                throw new BadRequestException("Invalid username or password");
            }
            legacy.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(legacy);
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));

        String role = normalizeRole(user.getRole());
        String token = jwtService.generateToken(user.getUsername(), role);

        return new AuthResponse("Login successful", token, "Bearer", user.getUsername(), role);
    }

    public AuthResponse register(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new BadRequestException("Username is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }

        if (userRepository.existsByUsername(request.getUsername().trim())) {
            throw new BadRequestException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(normalizeRole(request.getRole()));
        user.setProvider("LOCAL");
        if (request.getUsername().contains("@")) {
            user.setEmail(request.getUsername().trim().toLowerCase());
        }

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse("User registered successfully", token, "Bearer", user.getUsername(), user.getRole());
    }

    public AuthResponse socialAuth(SocialAuthRequest request, String provider) {
        String email = request.getEmail().trim().toLowerCase();
        String username = request.getUsername();
        if (username == null || username.isBlank()) {
            username = email;
        }
        final String resolvedUsername = username;

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(resolvedUsername);
                    newUser.setPassword(passwordEncoder.encode("social-login"));
                    newUser.setProvider(provider);
                    newUser.setRole(normalizeRole(request.getRole()));
                    return userRepository.save(newUser);
                });

        if (user.getProvider() == null || user.getProvider().isBlank()) {
            user.setProvider(provider);
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            user.setEmail(email);
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole(normalizeRole(request.getRole()));
        }
        userRepository.save(user);

        String role = normalizeRole(user.getRole());
        String token = jwtService.generateToken(user.getUsername(), role);
        return new AuthResponse(provider + " login successful", token, "Bearer", user.getUsername(), role);
    }

    public Map<String, Object> checkEmail(String email) {
        if (email == null || email.isBlank()) {
            return Map.of("exists", false, "available", true);
        }
        boolean exists = userRepository.existsByEmail(email.trim().toLowerCase());
        return Map.of("exists", exists, "available", !exists);
    }

    public Map<String, Object> checkUsername(String username) {
        if (username == null || username.isBlank()) {
            return Map.of("exists", false, "available", true);
        }
        boolean exists = userRepository.existsByUsername(username.trim());
        return Map.of("exists", exists, "available", !exists);
    }

    public Map<String, Object> logout() {
        return Map.of("message", "Logout successful");
    }

    public Map<String, Object> verifyToken(String token) {
        boolean valid = jwtService.isTokenValid(token);
        String username = valid ? jwtService.extractUsername(token) : null;
        String role = valid ? jwtService.extractRole(token) : null;
        return Map.of(
                "valid", valid,
                "username", username == null ? "" : username,
                "role", role == null ? "" : role
        );
    }

    public AuthResponse setRole(SetRoleRequest request, String authenticatedUsername) {
        String username = request.getUsername();
        if (username == null || username.isBlank()) {
            username = authenticatedUsername;
        }
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username is required");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(normalizeRole(request.getRole()));
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse("Role updated", token, "Bearer", user.getUsername(), user.getRole());
    }

    public Map<String, Object> forgotPassword(ForgotPasswordRequest request) {
        User user = resolveUserForPasswordOps(request.getUsername(), request.getEmail());
        if (user == null) {
            return Map.of("message", "If account exists, reset instructions sent", "ok", true);
        }
        return Map.of("message", "Account verified. Call /auth/reset-password with new password", "ok", true);
    }

    public Map<String, Object> resetPassword(ResetPasswordRequest request) {
        User user = resolveUserForPasswordOps(request.getUsername(), request.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return Map.of("message", "Password reset successful", "ok", true);
    }

    private User resolveUserForPasswordOps(String username, String email) {
        if (username != null && !username.isBlank()) {
            return userRepository.findByUsername(username.trim()).orElse(null);
        }
        if (email != null && !email.isBlank()) {
            return userRepository.findByEmail(email.trim().toLowerCase()).orElse(null);
        }
        return null;
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "PATIENT";
        }
        String normalized = role.trim().toUpperCase();
        if (!VALID_ROLES.contains(normalized)) {
            throw new BadRequestException("Invalid role. Allowed roles: DOCTOR, NURSE, PATIENT");
        }
        return normalized;
    }
}
