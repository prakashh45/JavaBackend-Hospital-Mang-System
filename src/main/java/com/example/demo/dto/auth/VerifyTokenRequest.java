package com.example.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;
public class VerifyTokenRequest {
    @NotBlank
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
