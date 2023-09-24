package com.reservation.backend.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservation.backend.entities.User;
import com.reservation.backend.enums.Role;
import com.reservation.backend.exceptions.UserRegisterException;
import com.reservation.backend.repositories.UserRepository;
import com.reservation.backend.requests.AuthenticationRequest;
import com.reservation.backend.requests.RegisterRequest;
import com.reservation.backend.responses.AuthenticationResponse;
import com.reservation.backend.security.JwtService;
import com.reservation.backend.services.AuthenticationService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    User user = new User(1L, "test", "test", "test@gmail.com", "test1234", LocalDate.now(), Role.USER);

    @Test
    public void registerUser_Success() throws Exception {
        Mockito.when(userRepository.findAll()).thenReturn(List.of());
        Mockito.when(jwtService.generateToken(user)).thenReturn("token");

        RegisterRequest rr = new RegisterRequest();
        rr.setFirstname("test");
        rr.setLastname("test");
        rr.setEmail("test@gmail.com");
        rr.setPassword("test1234");

        AuthenticationResponse ar = authenticationService.register(rr);

        Mockito.verify(userRepository, Mockito.times(1)).save(user);

        Assertions.assertThat(ar.getToken()).isEqualTo("token");
    }

    @Test
    public void registerUser_EmailAlreadyExists_Failure() throws Exception {
        Mockito.when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        RegisterRequest rr = new RegisterRequest();
        rr.setFirstname("test");
        rr.setLastname("test");
        rr.setEmail("test@gmail.com");
        rr.setPassword("test1234");

        assertThrows(UserRegisterException.class, () -> authenticationService.register(rr));

        Mockito.verify(userRepository, Mockito.times(0)).save(user);
    }

    @Test
    public void loginUser_Success() throws Exception {
        Mockito.when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        Mockito.when(jwtService.generateToken(user)).thenReturn("token");

        AuthenticationRequest request = new AuthenticationRequest("test@gmail.com", "test1234");

        AuthenticationResponse response = authenticationService.authenticate(request);

        Assertions.assertThat(response.getToken()).isEqualTo("token");
    }

    @Test
    public void loginUser_InvalidEmail_Failure() throws Exception {
        Mockito.when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        AuthenticationRequest request = new AuthenticationRequest("test1@gmail.com", "test1234");

        assertThrows(Exception.class, () -> authenticationService.authenticate(request));
    }

    @Test
    public void loginUser_InvalidPassword_Failure() throws Exception {
        Mockito.when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        AuthenticationRequest request = new AuthenticationRequest("test@gmail.com", "test12345");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void loginUser_NotExistingUser_Failure() throws Exception {
        Mockito.when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        AuthenticationRequest request = new AuthenticationRequest("fail@gmail.com", "failPassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
