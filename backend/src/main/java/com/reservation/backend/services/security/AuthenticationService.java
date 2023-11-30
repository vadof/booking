package com.reservation.backend.services.security;

import com.reservation.backend.entities.User;
import com.reservation.backend.enums.Role;
import com.reservation.backend.exceptions.AppException;
import com.reservation.backend.repositories.UserRepository;
import com.reservation.backend.requests.AuthenticationRequest;
import com.reservation.backend.requests.RegisterRequest;
import com.reservation.backend.responses.AuthenticationResponse;
import com.reservation.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        validateEmail(request.getEmail());

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    private void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AppException("Email already in use", HttpStatus.BAD_REQUEST);
        } else if (!Pattern.matches("^(.+)@(\\S+)$", email)) {
            throw new AppException("Invalid email", HttpStatus.BAD_REQUEST);
        }
    }
}
