package com.example.gotogether.services;

import com.example.gotogether.dto.ReviewDTO;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.Review;
import com.example.gotogether.model.Ride;
import com.example.gotogether.model.User;
import com.example.gotogether.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @Autowired
    private final ReviewRepository reviewRepository;

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + id));
        return mapToDTO(review);
    }

    public ReviewDTO createReview(ReviewDTO dto) {
        Review review = new Review();
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        review.setReviewer(new User(dto.getReviewerId()));
        review.setReviewedUser(new User(dto.getReviewedUserId()));
        review.setRide(new Ride(dto.getRideId()));


        Review saved = reviewRepository.save(review);
        return mapToDTO(saved);
    }

    public ReviewDTO updateReview(Long id, ReviewDTO dto) {
        Review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + id));

        existing.setComment(dto.getComment());
        existing.setRating(dto.getRating());
        existing.setReviewer(new User(dto.getReviewerId()));
        existing.setReviewedUser(new User(dto.getReviewedUserId()));
        existing.setRide(new Ride(dto.getRideId()));

        Review updated = reviewRepository.save(existing);
        return mapToDTO(updated);
    }

    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found with id " + id);
        }
        reviewRepository.deleteById(id);
    }

    public ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setReviewerId(review.getReviewer().getId());
        dto.setReviewedUserId(review.getReviewedUser().getId());
        dto.setRideId(review.getRide().getId());
        return dto;
    }
}
