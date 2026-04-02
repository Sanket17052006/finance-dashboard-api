package com.sanket.financedashboardapi.service;

import com.sanket.financedashboardapi.dto.request.UpdateRoleRequest;
import com.sanket.financedashboardapi.dto.request.UpdateStatusRequest;
import com.sanket.financedashboardapi.dto.response.UserResponse;
import com.sanket.financedashboardapi.exception.ResourceNotFoundException;
import com.sanket.financedashboardapi.model.User;
import com.sanket.financedashboardapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public UserResponse updateRole(String id, UpdateRoleRequest request) {
        User user = findById(id);
        user.setRole(request.getRole());
        user.setUpdatedAt(LocalDateTime.now());
        return toResponse(userRepository.save(user));
    }

    public UserResponse updateStatus(String id, UpdateStatusRequest request) {
        User user = findById(id);
        user.setStatus(request.getStatus());
        user.setUpdatedAt(LocalDateTime.now());
        return toResponse(userRepository.save(user));
    }

    private User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}