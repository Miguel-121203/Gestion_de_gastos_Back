package com.example.ms_user.application.usecases;

import com.example.ms_user.domain.model.AuthProvider;
import com.example.ms_user.domain.model.Role;
import com.example.ms_user.domain.model.User;
import com.example.ms_user.domain.model.dto.AuthResponse;
import com.example.ms_user.domain.model.dto.LoginRequest;
import com.example.ms_user.domain.model.dto.RegisterRequest;
import com.example.ms_user.domain.model.dto.UserResponse;
import com.example.ms_user.domain.ports.AuthServicePort;
import com.example.ms_user.domain.ports.UserRepositoryPort;
import com.example.ms_user.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case implementation for authentication operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUseCase implements AuthServicePort {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check if email already exists
        if (userRepositoryPort.existsByEmail(request.getEmail())) {
            log.error("Email already registered: {}", request.getEmail());
            throw new IllegalArgumentException("Email already registered");
        }

        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .provider(AuthProvider.LOCAL)
                .role(Role.USER)
                .active(true)
                .emailVerified(false)
                .build();

        // Save user
        User savedUser = userRepositoryPort.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getUserId());

        // Generate JWT token
        String token = jwtService.generateToken(savedUser);

        // Build response
        return buildAuthResponse(savedUser, token);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("User login attempt with email: {}", request.getEmail());

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Get user from database
            User user = userRepositoryPort.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

            if (!user.getActive()) {
                throw new BadCredentialsException("Account is deactivated");
            }

            // Generate JWT token
            String token = jwtService.generateToken(user);

            log.info("User logged in successfully: {}", user.getEmail());

            // Build response
            return buildAuthResponse(user, token);

        } catch (BadCredentialsException e) {
            log.error("Failed login attempt for email: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    @Override
    public String extractEmailFromToken(String token) {
        return jwtService.extractEmail(token);
    }

    @Override
    public Long extractUserIdFromToken(String token) {
        return jwtService.extractUserId(token);
    }

    /**
     * Process OAuth2 user and create or update user
     */
    @Transactional
    public AuthResponse processOAuth2User(String email, String firstName, String lastName,
                                          String providerId, String profilePictureUrl) {
        log.info("Processing OAuth2 user: {}", email);

        User user = userRepositoryPort.findByEmail(email)
                .orElseGet(() -> {
                    // Create new user from OAuth2 data
                    User newUser = User.builder()
                            .email(email)
                            .firstName(firstName)
                            .lastName(lastName)
                            .provider(AuthProvider.GOOGLE)
                            .providerId(providerId)
                            .profilePictureUrl(profilePictureUrl)
                            .role(Role.USER)
                            .active(true)
                            .emailVerified(true) // OAuth2 emails are pre-verified
                            .build();

                    log.info("Creating new user from OAuth2: {}", email);
                    return userRepositoryPort.save(newUser);
                });

        // Update existing user if needed
        if (!user.getEmailVerified()) {
            user.verifyEmail();
            user = userRepositoryPort.save(user);
        }

        // Generate JWT token
        String token = jwtService.generateToken(user);

        log.info("OAuth2 user processed successfully: {}", email);

        return buildAuthResponse(user, token);
    }

    /**
     * Build authentication response
     */
    private AuthResponse buildAuthResponse(User user, String token) {
        UserResponse userResponse = UserResponse.builder()
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

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(userResponse)
                .build();
    }
}
