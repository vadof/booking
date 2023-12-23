package com.reservation.backend.services;

import com.reservation.backend.dto.UserReviewDTO;
import com.reservation.backend.entities.User;
import com.reservation.backend.entities.UserReview;
import com.reservation.backend.mapper.UserReviewMapper;
import com.reservation.backend.repositories.UserReviewRepository;
import com.reservation.backend.services.common.GenericService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReviewService extends GenericService {

    private final UserReviewRepository userReviewRepository;
    private final UserReviewMapper userReviewMapper;

    public UserReviewDTO saveReviewUser(UserReviewDTO userReviewDTO) {
        User user = getCurrentUserAsEntity();
        UserReview userReview  = userReviewMapper.toEntity(userReviewDTO);
        userReview.setUser(user);

        UserReview userReviewSaved = userReviewRepository.save(userReview);
        return userReviewMapper.toDto(userReviewSaved);
    }
}