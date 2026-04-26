package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.auth.ForgotPasswordRequest;
import com.example.demo.dto.auth.ResetPasswordRequest;
import com.example.demo.dto.auth.SetRoleRequest;
import com.example.demo.dto.auth.SocialAuthRequest;
import com.example.demo.dto.auth.VerifyTokenRequest;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody LoginRequest request) {
        return authService.register(request);
    }

    @PostMapping("/google")
    public AuthResponse google(@Valid @RequestBody SocialAuthRequest request) {
        return authService.socialAuth(request, "GOOGLE");
    }

    @PostMapping("/apple")
    public AuthResponse apple(@Valid @RequestBody SocialAuthRequest request) {
        return authService.socialAuth(request, "APPLE");
    }

    @GetMapping("/check-email")
    public Map<String, Object> checkEmail(@RequestParam("email") String email) {
        return authService.checkEmail(email);
    }

    @GetMapping("/check-username")
    public Map<String, Object> checkUsername(@RequestParam("username") String username) {
        return authService.checkUsername(username);
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
        return authService.logout();
    }

    @PostMapping("/verify")
    public Map<String, Object> verify(@Valid @RequestBody VerifyTokenRequest request) {
        return authService.verifyToken(request.getToken());
    }

    @PostMapping("/set-role")
    @PreAuthorize("hasAnyRole('DOCTOR','NURSE','PATIENT')")
    public AuthResponse setRole(@Valid @RequestBody SetRoleRequest request, Authentication authentication) {
        String username = authentication == null ? null : authentication.getName();
        return authService.setRole(request, username);
    }

    @PostMapping("/forgot-password")
    public Map<String, Object> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public Map<String, Object> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(request);
    }

    @GetMapping("/me")
    public Map<String, String> me(Authentication authentication) {
        Map<String, String> response = new HashMap<>();
        response.put("username", authentication.getName());

        String role = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_PATIENT")
                .replace("ROLE_", "");
        response.put("role", role);

        return response;
    }
}
