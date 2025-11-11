package com.example.ms_user.infrastructure.configuration;

import com.example.ms_user.application.usecases.AuthUseCase;
import com.example.ms_user.application.usecases.UserUseCase;
import com.example.ms_user.domain.ports.AuthServicePort;
import com.example.ms_user.domain.ports.UserRepositoryPort;
import com.example.ms_user.domain.ports.UserServicePort;
import com.example.ms_user.infrastructure.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean configuration for hexagonal architecture
 * Wiring ports and adapters
 */
@Configuration
public class BeanConfiguration {

    /**
     * Configure AuthServicePort bean
     */
    @Bean
    public AuthServicePort authServicePort(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        return new AuthUseCase(userRepositoryPort, passwordEncoder, jwtService, authenticationManager);
    }

    /**
     * Configure UserServicePort bean
     */
    @Bean
    public UserServicePort userServicePort(UserRepositoryPort userRepositoryPort) {
        return new UserUseCase(userRepositoryPort);
    }
}
