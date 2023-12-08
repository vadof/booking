package com.reservation.backend.review;

import com.reservation.backend.GenericTest;
import com.reservation.backend.dto.ReviewDTO;
import com.reservation.backend.dto.UserDTO;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.Review;
import com.reservation.backend.entities.User;
import com.reservation.backend.enums.Role;
import com.reservation.backend.mapper.ReviewMapper;
import com.reservation.backend.mapper.ReviewMapperImpl;
import com.reservation.backend.mapper.UserMapper;
import com.reservation.backend.mapper.UserMapperImpl;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.ReviewRepository;
import com.reservation.backend.services.ReviewService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ReviewTests extends GenericTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private HousingRepository housingRepository;

    @Spy
    private ReviewMapper reviewMapper = new ReviewMapperImpl();

    @InjectMocks
    private ReviewService reviewService;


    @BeforeEach
    private void iniMapperDependencies() {
        UserMapper userMapper = new UserMapperImpl();
        ReflectionTestUtils.setField(reviewMapper, "userMapper", userMapper);
    }

    User reviewer = new User(1L, "test", "test", "test@gmail.com", "test1234", List.of(), LocalDate.now(), Role.USER);

    @Test
    void testSaveReview() {
        ReviewDTO reviewDTO = new ReviewDTO();

        User reviewer = User.builder().id(2L).email("email1").build();
        User owner = User.builder().id(1L).email("email2").build();

        Housing housing = Housing.builder().id(1L).build();
        housing.setReviews(new ArrayList<>());
        housing.setOwner(owner);

        mockAuthenticatedUser(reviewer);
        Mockito.when(housingRepository.findByIdAndPublishedTrue(housing.getId())).thenReturn(Optional.of(housing));
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
        mockAuthenticatedUser(reviewer);
//        when(reviewMapper.toDto(existingReview)).thenReturn(updatedReviewDTO);

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
        mockAuthenticatedUser(reviewer);
//        when(reviewService.getCurrentUserAsEntity()).thenReturn(reviewer);
//        when(reviewMapper.toDto(review)).thenReturn(new ReviewDTO(/* initialize with data */));

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



