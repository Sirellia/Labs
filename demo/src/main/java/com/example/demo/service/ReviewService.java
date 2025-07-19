package com.example.demo.service;

import com.example.demo.dto.ReviewRequestDTO;
import com.example.demo.dto.ReviewResponseDTO;
import com.example.demo.model.Restaurant;
import com.example.demo.model.Review;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public ReviewResponseDTO save(ReviewRequestDTO dto) {
        Review review = new Review(dto.visitorId(), dto.restaurantId(), dto.score(), dto.comment());
        reviewRepository.save(review);
        recalculateRestaurantRating(dto.restaurantId());
        return toResponseDTO(review);
    }

    public void remove(Long visitorId, Long restaurantId) {
        reviewRepository.findById(visitorId, restaurantId)
                .ifPresent(r -> {
                    reviewRepository.remove(r);
                    recalculateRestaurantRating(restaurantId);
                });
    }

    public List<ReviewResponseDTO> findAll() {
        return reviewRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReviewResponseDTO> findById(Long visitorId, Long restaurantId) {
        return reviewRepository.findById(visitorId, restaurantId)
                .map(this::toResponseDTO);
    }

    public ReviewResponseDTO update(Long visitorId, Long restaurantId, ReviewRequestDTO dto) {
        Optional<Review> optional = reviewRepository.findById(visitorId, restaurantId);
        if (optional.isPresent()) {
            Review review = optional.get();
            review.setScore(dto.score());
            review.setComment(dto.comment());
            recalculateRestaurantRating(restaurantId);
            return toResponseDTO(review);
        }
        throw new IllegalArgumentException("Review not found");
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        List<Review> reviews = reviewRepository.findAll();
        double avg = reviews.stream()
                .filter(r -> r.getRestaurantId() != null && r.getRestaurantId().equals(restaurantId))
                .mapToInt(Review::getScore)
                .average()
                .orElse(0.0);
        for (Restaurant restaurant : restaurantRepository.findAll()) {
            if (restaurant.getId() != null && restaurant.getId().equals(restaurantId)) {
                restaurant.setRating(BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP));
            }
        }
    }

    private ReviewResponseDTO toResponseDTO(Review review) {
        return new ReviewResponseDTO(review.getVisitorId(), review.getRestaurantId(), review.getScore(), review.getComment());
    }
} 
