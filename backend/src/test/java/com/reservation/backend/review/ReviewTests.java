package com.reservation.backend.review;

import com.reservation.backend.GenericTest;
import com.reservation.backend.dto.ReviewDTO;
import com.reservation.backend.dto.UserDTO;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.Review;
import com.reservation.backend.entities.User;
import com.reservation.backend.enums.Role;
import com.reservation.backend.exceptions.AppException;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
class ReviewTests extends GenericTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private HousingRepository housingRepository;

    @Spy
    private ReviewMapper reviewMapper = new ReviewMapperImpl();

    @Spy
    private UserMapper userMapper;

    @InjectMocks
    private ReviewService reviewService;


    @BeforeEach
    private void iniMapperDependencies() {
        UserMapper userMapper = new UserMapperImpl();
        ReflectionTestUtils.setField(reviewMapper, "userMapper", userMapper);
    }


    @Test
    @Transactional
    void testSaveReviewSuccessful() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setRating(10);

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
    @Transactional
    void testSaveReviewHousingNotFound() {
        ReviewDTO reviewDTO = new ReviewDTO();

        User reviewer = User.builder().id(2L).email("email1").build();
        User owner = User.builder().id(1L).email("email2").build();

        Housing housing = Housing.builder().id(1L).build();
        housing.setReviews(new ArrayList<>());
        housing.setOwner(owner);
        Housing housing2 = Housing.builder().id(2L).build();
        Long housing2Id = 2L;

        mockAuthenticatedUser(reviewer);
        Mockito.when(housingRepository.findByIdAndPublishedTrue(housing.getId())).thenReturn(Optional.of(housing));
        Mockito.when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppException exception = assertThrows(
                AppException.class,
                () -> reviewService.saveReview(housing2Id, reviewDTO)
        );

        assertEquals("Housing with id 2 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());

        verify(housingRepository, times(1)).findByIdAndPublishedTrue(housing2.getId());
        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toDto(any());
    }

    @Test
    @Transactional
    void testSaveReviewOwnerCanNotLeaveAReview() {
        ReviewDTO reviewDTO = new ReviewDTO();

        User owner = User.builder().id(1L).email("email2").build();

        Housing housing = Housing.builder().id(1L).build();
        Long housingId = 1L;
        housing.setReviews(new ArrayList<>());
        housing.setOwner(owner);

        mockAuthenticatedUser(owner);
        Mockito.when(housingRepository.findByIdAndPublishedTrue(housing.getId())).thenReturn(Optional.of(housing));
        Mockito.when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppException exception = assertThrows(
                AppException.class,
                () -> reviewService.saveReview(housingId, reviewDTO)
        );

        assertEquals("The owner cannot leave reviews", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());

        verify(housingRepository, times(1)).findByIdAndPublishedTrue(housing.getId());
        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toDto(any());
    }

    @Test
    @Transactional
    void testSaveReviewCanNotLeaveAnotherReview() {
        User reviewer = User.builder().id(2L).firstname("test").lastname("test").email("email1").build();
        User owner = User.builder().id(1L).email("email2").build();
        UserDTO reviewerDTO = new UserDTO(1L, "test", "test", "email1");

        ReviewDTO reviewDTO = new ReviewDTO(1L, "uus", 9, LocalDate.now(), reviewerDTO);


        Housing housing = Housing.builder().id(1L).build();
        Long housingId = 1L;
        Review review = new Review(1L, "uus", 9, LocalDate.now(), reviewer, housing);
        housing.setReviews(List.of(review));
        housing.setOwner(owner);

        mockAuthenticatedUser(reviewer);
        Mockito.when(housingRepository.findByIdAndPublishedTrue(housing.getId())).thenReturn(Optional.of(housing));
        Mockito.when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppException exception = assertThrows(
                AppException.class,
                () -> reviewService.saveReview(housingId, reviewDTO)
        );

        assertEquals("Review has already been left", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());

        verify(housingRepository, times(1)).findByIdAndPublishedTrue(housing.getId());
        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toDto(any());
    }


    @Test
    void testFindReviewById() {
        Long reviewId = 1L;
        UserDTO reviewerDTO = new UserDTO(1L, "test", "test", "test@gmail.com");
        User reviewer = new User(1L, "test", "test", "test@gmail.com", "test1234", List.of(), LocalDate.now(), Role.USER);
        Housing housing = new Housing();
        Review review = new Review(1L, "vaffa", 8, LocalDate.now(), reviewer, housing);
        ReviewDTO reviewDTO = new ReviewDTO(1L, "vaffa", 8, LocalDate.now(), reviewerDTO);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewMapper.toDto(review)).thenReturn(reviewDTO);

        ReviewDTO foundReviewDTO = reviewService.findReviewById(reviewId);

        assertEquals(reviewDTO, foundReviewDTO);

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewMapper, times(1)).toDto(review);
    }

    @Test
    void testFindReviewByIdCanNotFind() {
        Long id = 1L;
        UserDTO reviewerDTO = new UserDTO(1L, "test", "test", "test@gmail.com");
        User reviewer = new User(1L, "test", "test", "test@gmail.com", "test1234", List.of(), LocalDate.now(), Role.USER);
        Housing housing = new Housing();
        Review review = new Review(1L, "vaffa", 8, LocalDate.now(), reviewer, housing);
        ReviewDTO reviewDTO = new ReviewDTO(1L, "vaffa", 8, LocalDate.now(), reviewerDTO);
        when(reviewMapper.toDto(review)).thenReturn(reviewDTO);

        AppException exception = assertThrows(
                AppException.class,
                () -> reviewService.findReviewById(id)
        );

        assertEquals("Review with id 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());

        verify(reviewMapper, never()).toDto(any());
    }

    @Test
    @Transactional
    void testUpdateReview() {
        User reviewer = User.builder().id(2L).firstname("test").lastname("test").email("email1").build();
        User owner = User.builder().id(1L).email("email2").build();
        UserDTO reviewerDTO = new UserDTO(2L, "test", "test", "email1");

        Housing housing = Housing.builder().id(1L).build();
        housing.setReviews(new ArrayList<>());
        housing.setOwner(owner);

        ReviewDTO updatedReviewDTO = new ReviewDTO(1L, "uus", 9, LocalDate.now(), reviewerDTO);
        Review existingReview = new Review(1L, "vaffa", 8, LocalDate.now(), reviewer, housing);

        mockAuthenticatedUser(reviewer);
        when(housingRepository.findByIdAndPublishedTrue(housing.getId())).thenReturn(Optional.of(housing));
        when(reviewRepository.findById(updatedReviewDTO.getId())).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewDTO resultReviewDTO = reviewService.updateReview(updatedReviewDTO);

        assertEquals(updatedReviewDTO, resultReviewDTO);

        verify(reviewRepository, times(1)).save(existingReview);
        verify(reviewMapper, times(1)).toDto(existingReview);
    }

    @Test
    @Transactional
    void testUpdateReviewOnlyReviewerCanChangeTheReview() {
        User reviewer = User.builder().id(2L).firstname("test").lastname("test").email("email1").build();
        User randomUser = User.builder().id(3L).build();
        User owner = User.builder().id(1L).email("email2").build();
        UserDTO reviewerDTO = new UserDTO(2L, "test", "test", "email1");

        Housing housing = Housing.builder().id(1L).build();
        housing.setReviews(new ArrayList<>());
        housing.setOwner(owner);

        ReviewDTO updatedReviewDTO = new ReviewDTO(1L, "uus", 9, LocalDate.now(), reviewerDTO);
        Review existingReview = new Review(1L, "vaffa", 8, LocalDate.now(), reviewer, housing);

        mockAuthenticatedUser(randomUser);
        when(housingRepository.findByIdAndPublishedTrue(housing.getId())).thenReturn(Optional.of(housing));
        when(reviewRepository.findById(updatedReviewDTO.getId())).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppException exception = assertThrows(
                AppException.class,
                () -> reviewService.updateReview(updatedReviewDTO)
        );

        assertEquals("Access to update review denied", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());

        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toDto(any());
    }


    @Test
    @Transactional
    void testDeleteReview() {
        User reviewer = User.builder().id(2L).firstname("test").lastname("test").email("email1").build();
        User owner = User.builder().id(1L).email("email2").build();
        UserDTO reviewerDTO = new UserDTO(2L, "test", "test", "email1");

        Housing housing = Housing.builder().id(1L).build();
        housing.setReviews(new ArrayList<>());
        housing.setOwner(owner);

        Review review = new Review(1L, "vaffa", 8, LocalDate.now(), reviewer, housing);
        ReviewDTO reviewDTO = new ReviewDTO(1L, "vaffa", 8, LocalDate.now(), reviewerDTO);

        mockAuthenticatedUser(reviewer);
        when(housingRepository.findByIdAndPublishedTrue(housing.getId())).thenReturn(Optional.of(housing));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(reviewRepository.findById(reviewDTO.getId())).thenReturn(Optional.of(review));


        ReviewDTO deletedReviewDTO = reviewService.deleteReview(review.getId());

        assertEquals(reviewDTO, deletedReviewDTO);

        verify(reviewRepository, times(1)).delete(review);
        verify(reviewMapper, times(1)).toDto(review);
    }

    @Test
    @Transactional
    void testDeleteReviewUnauthorizedUserCanNotDelete() {
        Long reviewId = 1L;
        User reviewer = User.builder().id(2L).firstname("test").lastname("test").email("email1").build();
        User owner = User.builder().id(1L).email("email2").build();
        UserDTO reviewerDTO = new UserDTO(2L, "test", "test", "email1");

        Housing housing = Housing.builder().id(1L).build();
        housing.setReviews(new ArrayList<>());
        housing.setOwner(owner);

        Review review = new Review(1L, "vaffa", 8, LocalDate.now(), reviewer, housing);
        ReviewDTO reviewDTO = new ReviewDTO(1L, "vaffa", 8, LocalDate.now(), reviewerDTO);

        mockAuthenticatedUser(owner);
        when(housingRepository.findByIdAndPublishedTrue(housing.getId())).thenReturn(Optional.of(housing));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(reviewRepository.findById(reviewDTO.getId())).thenReturn(Optional.of(review));

        AppException exception = assertThrows(
                AppException.class,
                () -> reviewService.deleteReview(reviewId)
        );

        assertEquals("Access to delete review denied", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());

        verify(reviewRepository, times(1)).findById(review.getId());
        verify(reviewRepository, never()).delete(any());
        verify(reviewMapper, never()).toDto(any());
    }
}
