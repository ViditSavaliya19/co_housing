package com.example.co_housing.controller;

import com.example.co_housing.entity.UserData;
import com.example.co_housing.model.ApiResponse;
import com.example.co_housing.model.JwtResponse;
import com.example.co_housing.model.LoginRequest;
import com.example.co_housing.model.RegisterRequest;
import com.example.co_housing.repository.UserRepository;
import com.example.co_housing.security.JwtUtil;
import com.example.co_housing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
           var token = userService.register(request);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody LoginRequest request) {
        try {
            JwtResponse token = userService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<ApiResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Missing or invalid Authorization header"));
        }

        String token = authHeader.substring(7); // remove "Bearer "

        boolean isValid = userService.validateToken(token);
        if (isValid) {
            return ResponseEntity.ok( ApiResponse.success("Token is valid",null));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Invalid or expired token"));
        }
    }
}

