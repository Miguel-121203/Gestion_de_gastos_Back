package com.example.ms_user.domain.ports;

import com.example.ms_user.domain.model.dto.UpdateUserRequest;
import com.example.ms_user.domain.model.dto.UserResponse;

import java.util.List;

/**
 * Port interface for user service operations
 */
public interface UserServicePort {

    /**
     * Get user by ID
     */
    UserResponse getUserById(Long userId);

    /**
     * Get user by email
     */
    UserResponse getUserByEmail(String email);

    /**
     * Get all active users
     */
    List<UserResponse> getAllActiveUsers();

    /**
     * Update user information
     */
    UserResponse updateUser(Long userId, UpdateUserRequest request);

    /**
     * Delete user (soft delete)
     */
    void deleteUser(Long userId);

    /**
     * Activate user
     */
    void activateUser(Long userId);

    /**
     * Deactivate user
     */
    void deactivateUser(Long userId);
}
