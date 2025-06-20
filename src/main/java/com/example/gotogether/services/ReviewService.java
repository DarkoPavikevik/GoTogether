package com.example.gotogether.services;

import com.example.gotogether.dto.ReviewDTO;
import com.example.gotogether.exceptions.ResourceNotFoundException;
import com.example.gotogether.model.Review;
import com.example.gotogether.model.Ride;
import com.example.gotogether.model.User;
import com.example.gotogether.repositories.ReviewRepository;
import com.example.gotogether.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EmailService emailService;

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Page<ReviewDTO> getReviewById(Long id, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByReviewedUserId(id, pageable);
        return reviews.map(this::mapToDTO);
    }

    public ReviewDTO createReview(ReviewDTO dto) {

        User reviewer = userRepository.findById(dto.getReviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found"));

        User reviewedUser = userRepository.findById(dto.getReviewedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Reviewed user not found"));

        Review review = new Review();
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        review.setReviewer(reviewer);
        review.setReviewedUser(reviewedUser);
        review.setCommentDate(dto.getCommentDate());


        Review saved = reviewRepository.save(review);

        updateUserRating(reviewedUser.getId());


        return mapToDTO(saved);
    }

    public ReviewDTO updateReview(Long id, ReviewDTO dto) {
        Review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + id));

        existing.setComment(dto.getComment());
        existing.setRating(dto.getRating());
        existing.setReviewer(new User(dto.getReviewerId()));
        existing.setReviewedUser(new User(dto.getReviewedUserId()));
        existing.setCommentDate(dto.getCommentDate());

        Review updated = reviewRepository.save(existing);
        return mapToDTO(updated);
    }

    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found with id " + id);
        }
        reviewRepository.deleteById(id);
    }

    private void updateUserRating(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Integer totalRating = reviewRepository.sumRatingsByReviewedUserId(userId);
        int numberOfReviews = reviewRepository.countByReviewedUserId(userId);

        if (totalRating == null || numberOfReviews == 0) {
            user.setRating(0.0);
        } else {
            double newRating = Math.round(((double) totalRating / numberOfReviews) * 10.0) / 10.0;
            user.setRating(newRating);
        }
        if (totalRating < 1.5) {
            emailService.sendLowRatingAlert(user);
        }

        userRepository.save(user);
    }


    public ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setReviewerId(review.getReviewer().getId());
        dto.setReviewedUserId(review.getReviewedUser().getId());
        dto.setCommentDate(review.getCommentDate());
        dto.setReviewerName(review.getReviewer().getUsername());
        dto.setReviewerPicture(review.getReviewer().getProfilePicture());
        return dto;
    }
}
