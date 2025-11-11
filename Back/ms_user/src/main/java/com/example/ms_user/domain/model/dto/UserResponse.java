package com.example.ms_user.domain.model.dto;

import com.example.ms_user.domain.model.AuthProvider;
import com.example.ms_user.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user information response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private AuthProvider provider;
    private Role role;
    private Boolean emailVerified;
    private String profilePictureUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
