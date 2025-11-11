package com.example.ms_user.infrastructure.adapters.output.persistence;

import com.example.ms_user.domain.model.AuthProvider;
import com.example.ms_user.domain.model.User;
import com.example.ms_user.domain.ports.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter implementation for UserRepositoryPort using JPA
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        log.debug("Saving user: {}", user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long userId) {
        log.debug("Finding user by ID: {}", userId);
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByProviderAndProviderId(String provider, String providerId) {
        log.debug("Finding user by provider {} and providerId: {}", provider, providerId);
        try {
            AuthProvider authProvider = AuthProvider.valueOf(provider.toUpperCase());
            return userRepository.findByProviderAndProviderId(authProvider, providerId);
        } catch (IllegalArgumentException e) {
            log.error("Invalid provider: {}", provider);
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking if email exists: {}", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAllActive() {
        log.debug("Finding all active users");
        return userRepository.findByActiveTrue();
    }

    @Override
    public void delete(Long userId) {
        log.debug("Deleting user with ID: {}", userId);
        userRepository.findById(userId).ifPresent(user -> {
            user.deactivate();
            userRepository.save(user);
        });
    }
}
