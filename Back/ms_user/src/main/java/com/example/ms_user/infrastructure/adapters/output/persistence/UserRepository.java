package com.example.ms_user.infrastructure.adapters.output.persistence;

import com.example.ms_user.domain.model.AuthProvider;
import com.example.ms_user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by provider and provider ID
     */
    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Find all active users
     */
    List<User> findByActiveTrue();
}
