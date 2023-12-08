package com.reservation.backend.review;

import com.reservation.backend.dto.LocationDTO;
import com.reservation.backend.dto.ReviewDTO;
import com.reservation.backend.dto.UserDTO;
import com.reservation.backend.entities.*;
import com.reservation.backend.enums.Role;
import com.reservation.backend.mapper.LocationMapper;
import com.reservation.backend.mapper.ReviewMapper;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.LocationRepository;
import com.reservation.backend.repositories.ReviewRepository;
import com.reservation.backend.repositories.UserRepository;
import com.reservation.backend.requests.AuthenticationRequest;
import com.reservation.backend.requests.RegisterRequest;
import com.reservation.backend.responses.AuthenticationResponse;
import com.reservation.backend.security.JwtService;
import com.reservation.backend.services.LocationService;
import com.reservation.backend.services.ReviewService;
import com.reservation.backend.services.common.GenericService;
import com.reservation.backend.services.security.AuthenticationService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ReviewTests {

    @Autowired
    private MockMvc mockMvc;


    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private HousingRepository housingRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private GenericService genericService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;




    User reviewer = new User(1L, "test", "test", "test@gmail.com", "test1234", List.of(), LocalDate.now(), Role.USER);

    @Test
    @Transactional
    void testSaveReview() {
        ReviewDTO reviewDTO = new ReviewDTO();
        Housing housing = Housing.builder().id(1L).build();
        User reviewer = new User();
        reviewer.setId(2L);
        MockitoAnnotations.openMocks(this);

        Mockito.when(genericService.getCurrentUserAsEntity()).thenReturn(reviewer);
        Mockito.when(housingRepository.findByIdAndPublishedTrue(housing.getId())).thenReturn(Optional.of(housing));
        Mockito.when(reviewMapper.toEntity(reviewDTO)).thenReturn(new Review());
        Mockito.when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewDTO savedReview = reviewService.saveReview(housing.getId(), reviewDTO);

        assertNotNull(savedReview);
    }


    @Test
    public void testFindReviewById() {
        Long reviewId = 1L;
        UserDTO userDTO = new UserDTO(1L, "test", "test", "test@gmail.com");
        User reviewer = new User(1L, "test", "test", "test@gmail.com", "test1234", List.of(), LocalDate.now(), Role.USER);
        Housing housing = new Housing();
        Review review = new Review(1L, "vaffa", 8, LocalDate.now(), reviewer, housing);
        ReviewDTO reviewDTO = new ReviewDTO(1L, "vaffa", 8, LocalDate.now(), userDTO);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewMapper.toDto(review)).thenReturn(reviewDTO);

        ReviewDTO foundReviewDTO = reviewService.findReviewById(reviewId);

        assertEquals(reviewDTO, foundReviewDTO);

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewMapper, times(1)).toDto(review);
    }

    @Test
    @Transactional
    public void testUpdateReview() {
        // Arrange
        ReviewDTO updatedReviewDTO = new ReviewDTO(/* initialize with updated data */);
        Review existingReview = new Review(/* initialize with existing data */);
        User reviewer = new User(/* initialize with data */);
        when(reviewRepository.findById(updatedReviewDTO.getId())).thenReturn(Optional.of(existingReview));
        when(reviewService.getCurrentUserAsEntity()).thenReturn(reviewer);
        when(reviewMapper.toDto(existingReview)).thenReturn(updatedReviewDTO);

        // Act
        ReviewDTO resultReviewDTO = reviewService.updateReview(updatedReviewDTO);

        // Assert
        assertNotNull(resultReviewDTO);
        // Add more assertions based on your expected behavior

        // Verify that the repository's save method was called once
        verify(reviewRepository, times(1)).save(existingReview);
        verify(reviewMapper, times(1)).toDto(existingReview);
    }

    @Test
    @Transactional
    public void testDeleteReview() {
        // Arrange
        Long reviewId = 1L;
        Review review = new Review(/* initialize with data */);
        User reviewer = new User(/* initialize with data */);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewService.getCurrentUserAsEntity()).thenReturn(reviewer);
        when(reviewMapper.toDto(review)).thenReturn(new ReviewDTO(/* initialize with data */));

        // Act
        ReviewDTO deletedReviewDTO = reviewService.deleteReview(reviewId);

        // Assert
        assertNotNull(deletedReviewDTO);
        // Add more assertions based on your expected behavior

        // Verify that the repository's delete method was called once
        verify(reviewRepository, times(1)).delete(review);
        verify(reviewMapper, times(1)).toDto(review);
    }
}



