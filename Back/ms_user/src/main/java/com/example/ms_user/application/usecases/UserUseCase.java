package com.example.ms_user.application.usecases;

import com.example.ms_user.domain.model.User;
import com.example.ms_user.domain.model.dto.UpdateUserRequest;
import com.example.ms_user.domain.model.dto.UserResponse;
import com.example.ms_user.domain.ports.UserRepositoryPort;
import com.example.ms_user.domain.ports.UserServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use case implementation for user operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserUseCase implements UserServicePort {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        log.info("Getting user by ID: {}", userId);

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (!user.getActive()) {
            throw new IllegalArgumentException("User account is inactive");
        }

        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        log.info("Getting user by email: {}", email);

        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        if (!user.getActive()) {
            throw new IllegalArgumentException("User account is inactive");
        }

        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllActiveUsers() {
        log.info("Getting all active users");

        List<User> users = userRepositoryPort.findAllActive();

        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", userId);

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Update fields if provided
        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName());
        }

        if (request.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(request.getProfilePictureUrl());
        }

        User updatedUser = userRepositoryPort.save(user);
        log.info("User updated successfully with ID: {}", userId);

        return mapToUserResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.deactivate();
        userRepositoryPort.save(user);

        log.info("User deleted (soft delete) successfully with ID: {}", userId);
    }

    @Override
    @Transactional
    public void activateUser(Long userId) {
        log.info("Activating user with ID: {}", userId);

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.activate();
        userRepositoryPort.save(user);

        log.info("User activated successfully with ID: {}", userId);
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        log.info("Deactivating user with ID: {}", userId);

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.deactivate();
        userRepositoryPort.save(user);

        log.info("User deactivated successfully with ID: {}", userId);
    }

    /**
     * Map User entity to UserResponse DTO
     */
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .provider(user.getProvider())
                .role(user.getRole())
                .emailVerified(user.getEmailVerified())
                .profilePictureUrl(user.getProfilePictureUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
