package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null) {
            return "User not found ❌";
        }

        if (!user.getPassword().equals(request.getPassword())) {
            return "Wrong password ❌";
        }

        return "Login successful ✅";
    }

    // ✅ REGISTER (NEW)
    public String register(LoginRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        userRepository.save(user);

        return "User Registered Successfully ✅";
    }


}