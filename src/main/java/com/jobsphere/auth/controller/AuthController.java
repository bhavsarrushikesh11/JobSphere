package com.jobsphere.auth.controller;

import com.jobsphere.auth.dto.LoginRequest;
import com.jobsphere.auth.dto.RegisterRequest;
import com.jobsphere.auth.service.AuthService;
import com.jobsphere.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.jobsphere.auth.dto.AuthResponse;
import com.jobsphere.auth.dto.LoginRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
    public String test() {
        return "Auth public endpoint is working";
    }

    @PostMapping("/register")
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}