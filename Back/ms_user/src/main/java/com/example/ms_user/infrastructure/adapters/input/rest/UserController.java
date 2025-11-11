package com.example.ms_user.infrastructure.adapters.input.rest;

import com.example.ms_user.domain.model.dto.UpdateUserRequest;
import com.example.ms_user.domain.model.dto.UserResponse;
import com.example.ms_user.domain.ports.UserServicePort;
import com.example.ms_user.infrastructure.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for user management endpoints
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserServicePort userServicePort;

    /**
     * Get current authenticated user
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            log.info("Getting current user info for user ID: {}", userDetails.getUserId());
            UserResponse response = userServicePort.getUserById(userDetails.getUserId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting current user: {}", e.getMessage());
            return ResponseEntity.status(500).body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            log.info("Getting user by ID: {}", userId);
            UserResponse response = userServicePort.getUserById(userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(404).body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting user: {}", e.getMessage());
            return ResponseEntity.status(500).body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Get user by email
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            log.info("Getting user by email: {}", email);
            UserResponse response = userServicePort.getUserByEmail(email);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(404).body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting user: {}", e.getMessage());
            return ResponseEntity.status(500).body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Get all active users (Admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllActiveUsers() {
        try {
            log.info("Getting all active users");
            List<UserResponse> users = userServicePort.getAllActiveUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error getting all users: {}", e.getMessage());
            return ResponseEntity.status(500).body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Update user
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            log.info("Updating user with ID: {}", userId);
            UserResponse response = userServicePort.updateUser(userId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("User update failed: {}", e.getMessage());
            return ResponseEntity.status(404).body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.status(500).body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Delete user (soft delete)
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            log.info("Deleting user with ID: {}", userId);
            userServicePort.deleteUser(userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("User delete failed: {}", e.getMessage());
            return ResponseEntity.status(404).body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            return ResponseEntity.status(500).body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Activate user (Admin only)
     */
    @PutMapping("/{userId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        try {
            log.info("Activating user with ID: {}", userId);
            userServicePort.activateUser(userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User activated successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("User activation failed: {}", e.getMessage());
            return ResponseEntity.status(404).body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error activating user: {}", e.getMessage());
            return ResponseEntity.status(500).body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Deactivate user (Admin only)
     */
    @PutMapping("/{userId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId) {
        try {
            log.info("Deactivating user with ID: {}", userId);
            userServicePort.deactivateUser(userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deactivated successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("User deactivation failed: {}", e.getMessage());
            return ResponseEntity.status(404).body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error deactivating user: {}", e.getMessage());
            return ResponseEntity.status(500).body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Create error response
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
