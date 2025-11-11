package com.example.ms_user.infrastructure.adapters.input.rest;

import com.example.ms_user.application.usecases.AuthUseCase;
import com.example.ms_user.domain.model.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for OAuth2 authentication
 */
@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class OAuth2Controller {

    private final AuthUseCase authUseCase;

    /**
     * Handle successful OAuth2 authentication
     */
    @GetMapping("/success")
    public ResponseEntity<?> oauth2Success(@AuthenticationPrincipal OAuth2User principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(401)
                        .body(createErrorResponse("OAuth2 authentication failed"));
            }

            // Extract user information from OAuth2 provider
            String email = principal.getAttribute("email");
            String firstName = principal.getAttribute("given_name");
            String lastName = principal.getAttribute("family_name");
            String providerId = principal.getAttribute("sub");
            String pictureUrl = principal.getAttribute("picture");

            log.info("OAuth2 success for email: {}", email);

            // Process OAuth2 user (create or update)
            AuthResponse response = authUseCase.processOAuth2User(
                    email, firstName, lastName, providerId, pictureUrl
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("OAuth2 processing error: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(createErrorResponse("Error processing OAuth2 authentication"));
        }
    }

    /**
     * Handle OAuth2 authentication failure
     */
    @GetMapping("/failure")
    public ResponseEntity<?> oauth2Failure() {
        log.error("OAuth2 authentication failed");
        return ResponseEntity.status(401)
                .body(createErrorResponse("OAuth2 authentication failed"));
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
