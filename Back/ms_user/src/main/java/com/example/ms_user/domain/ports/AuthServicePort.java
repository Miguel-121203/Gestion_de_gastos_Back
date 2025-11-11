package com.example.ms_user.domain.ports;

import com.example.ms_user.domain.model.dto.AuthResponse;
import com.example.ms_user.domain.model.dto.LoginRequest;
import com.example.ms_user.domain.model.dto.RegisterRequest;

/**
 * Port interface for authentication service operations
 */
public interface AuthServicePort {

    /**
     * Register a new user
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Login a user with credentials
     */
    AuthResponse login(LoginRequest request);

    /**
     * Validate JWT token
     */
    boolean validateToken(String token);

    /**
     * Extract user email from token
     */
    String extractEmailFromToken(String token);

    /**
     * Extract user ID from token
     */
    Long extractUserIdFromToken(String token);
}
