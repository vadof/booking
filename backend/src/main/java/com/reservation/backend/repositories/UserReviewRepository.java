package com.reservation.backend.repositories;

import com.reservation.backend.entities.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

}
