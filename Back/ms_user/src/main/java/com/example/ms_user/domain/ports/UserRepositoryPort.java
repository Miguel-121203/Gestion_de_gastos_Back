package com.example.ms_user.domain.ports;

import com.example.ms_user.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for User repository operations
 */
public interface UserRepositoryPort {

    /**
     * Save a user
     */
    User save(User user);

    /**
     * Find user by ID
     */
    Optional<User> findById(Long userId);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by provider and provider ID
     */
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find all active users
     */
    List<User> findAllActive();

    /**
     * Delete user (soft delete)
     */
    void delete(Long userId);
}
