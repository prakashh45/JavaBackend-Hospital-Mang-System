package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String login(LoginRequest request) {
            if("admin".equals(request.getUsername())&& "1234".equals((request.getPassword()))){
                return "Login zalas bahav ";
            }
            return "part kar ..";



    }
}