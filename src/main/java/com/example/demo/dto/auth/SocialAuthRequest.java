package com.example.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialAuthRequest {
    @NotBlank
    private String email;
    private String username;
    private String role;
}
