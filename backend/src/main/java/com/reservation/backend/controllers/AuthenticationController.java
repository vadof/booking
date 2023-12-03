package com.reservation.backend.controllers;

import com.reservation.backend.requests.AuthenticationRequest;
import com.reservation.backend.requests.RegisterRequest;
import com.reservation.backend.responses.AuthenticationResponse;
import com.reservation.backend.services.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "API operations with Authentication")
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content(mediaType = "*/*")),
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        log.info("REST request to register user");
        return ResponseEntity.ok().body(authenticationService.register(request));
    }

    @Operation(summary = "Login to account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "403", description = "Invalid data", content = @Content(mediaType = "*/*")),
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        log.info("REST request to authenticate user {}", request.getEmail());
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
