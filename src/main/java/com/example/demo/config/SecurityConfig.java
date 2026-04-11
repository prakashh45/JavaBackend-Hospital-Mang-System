package com.example.demo.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    public static final String PUBLIC_AUTH_PATH = "/api/auth/**";
    public static final String AUTH_HEADER = "Authorization";
}
