package com.example.co_housing.service;

import com.example.co_housing.model.JwtResponse;
import com.example.co_housing.model.LoginRequest;
import com.example.co_housing.model.RegisterRequest;

public interface UserService {
    String register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
    String updateUser(Long id, RegisterRequest request);
    String deleteUser(Long id);
    String forgotPassword(String email);
    boolean validateToken(String token);
}
