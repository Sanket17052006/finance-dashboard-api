package com.sanket.financedashboardapi.service;

import com.sanket.financedashboardapi.dto.request.LoginRequest;
import com.sanket.financedashboardapi.dto.request.SignupRequest;
import com.sanket.financedashboardapi.dto.response.AuthResponse;
import com.sanket.financedashboardapi.enums.Role;
import com.sanket.financedashboardapi.enums.UserStatus;
import com.sanket.financedashboardapi.exception.DuplicateResourceException;
import com.sanket.financedashboardapi.exception.ResourceNotFoundException;
import com.sanket.financedashboardapi.exception.UnauthorizedActionException;
import com.sanket.financedashboardapi.model.User;
import com.sanket.financedashboardapi.repository.UserRepository;
import com.sanket.financedashboardapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());

        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.VIEWER)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }
}