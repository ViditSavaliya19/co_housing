package com.example.co_housing.service.impl;

import com.example.co_housing.entity.UserData;
import com.example.co_housing.model.JwtResponse;
import com.example.co_housing.model.LoginRequest;
import com.example.co_housing.model.RegisterRequest;
import com.example.co_housing.repository.UserRepository;
import com.example.co_housing.security.JwtUtil;
import com.example.co_housing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        UserData user = new UserData();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setDob(request.dob());
        user.setGender(request.gender());
        user.setBio(request.bio());
        user.setCity(request.city());
        user.setState(request.state());
        user.setCountry(request.country());
        user.setMobile(request.mobile());
        user.setProfession(request.profession());

        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        UserData user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new JwtResponse(token);
    }

    @Override
    public String updateUser(Long id, RegisterRequest request) {
        UserData user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.name());
        user.setDob(request.dob());
        user.setGender(request.gender());
        user.setBio(request.bio());
        user.setCity(request.city());
        user.setState(request.state());
        user.setCountry(request.country());
        user.setMobile(request.mobile());
        user.setProfession(request.profession());

        userRepository.save(user);
        return "User updated successfully";
    }

    @Override
    public String deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    @Override
    public String forgotPassword(String email) {
        UserData user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        // Simulate sending email with reset instructions
        return "Password reset instructions sent to " + email + " (simulated)";
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
