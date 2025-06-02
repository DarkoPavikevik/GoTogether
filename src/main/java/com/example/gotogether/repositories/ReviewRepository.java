package com.example.gotogether.repositories;

import com.example.gotogether.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByReviewedUserId(long userId, Pageable pageable);

    @Query("SELECT SUM(r.rating) FROM Review r WHERE r.reviewedUser.id = :userId")
    Integer sumRatingsByReviewedUserId(@Param("userId") Long userId);

    int countByReviewedUserId(Long userId);
}