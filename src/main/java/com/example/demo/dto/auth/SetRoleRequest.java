package com.example.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetRoleRequest {
    private String username;
    @NotBlank
    private String role;
}
