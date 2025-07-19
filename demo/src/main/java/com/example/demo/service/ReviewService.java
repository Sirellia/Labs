package com.example.demo.service;

import com.example.demo.dto.ReviewRequestDTO;
import com.example.demo.dto.ReviewResponseDTO;
import com.example.demo.model.Restaurant;
import com.example.demo.model.Review;
import com.example.demo.model.Visitor;
import com.example.demo.repository.RestaurantJpaRepository;
import com.example.demo.repository.ReviewJpaRepository;
import com.example.demo.repository.VisitorJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewJpaRepository reviewRepository;
    private final RestaurantJpaRepository restaurantRepository;
    private final VisitorJpaRepository visitorRepository;

    @Autowired
    public ReviewService(ReviewJpaRepository reviewRepository, RestaurantJpaRepository restaurantRepository, VisitorJpaRepository visitorRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
        this.visitorRepository = visitorRepository;
    }

    public ReviewResponseDTO save(ReviewRequestDTO dto) {
        Visitor visitor = visitorRepository.findById(dto.visitorId())
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found"));
        Restaurant restaurant = restaurantRepository.findById(dto.restaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        Review review = new Review(null, visitor, restaurant, dto.score(), dto.comment());
        review = reviewRepository.save(review);
        recalculateRestaurantRating(restaurant.getId());
        return toResponseDTO(review);
    }

    public void remove(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        reviewRepository.deleteById(reviewId);
        recalculateRestaurantRating(review.getRestaurant().getId());
    }

    public List<ReviewResponseDTO> findAll() {
        return reviewRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReviewResponseDTO> findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(this::toResponseDTO);
    }

    public ReviewResponseDTO update(Long reviewId, ReviewRequestDTO dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        review.setScore(dto.score());
        review.setComment(dto.comment());
        review = reviewRepository.save(review);
        recalculateRestaurantRating(review.getRestaurant().getId());
        return toResponseDTO(review);
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        List<Review> reviews = reviewRepository.findAll();
        double avg = reviews.stream()
                .filter(r -> r.getRestaurant().getId().equals(restaurantId))
                .mapToInt(Review::getScore)
                .average()
                .orElse(0.0);
        restaurantRepository.findById(restaurantId).ifPresent(restaurant -> {
            restaurant.setRating(BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP));
            restaurantRepository.save(restaurant);
        });
    }

    private ReviewResponseDTO toResponseDTO(Review review) {
        return new ReviewResponseDTO(
                review.getVisitor().getId(),
                review.getRestaurant().getId(),
                review.getScore(),
                review.getComment()
        );
    }
} 