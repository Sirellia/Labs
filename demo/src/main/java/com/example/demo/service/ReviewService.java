package com.example.demo.service;

import com.example.demo.model.Restaurant;
import com.example.demo.model.Review;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void save(Review review) {
        reviewRepository.save(review);
        recalculateRestaurantRating(review.getRestaurantId());
    }

    public void remove(Review review) {
        reviewRepository.remove(review);
        recalculateRestaurantRating(review.getRestaurantId());
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        List<Review> reviews = reviewRepository.findAll();
        double avg = reviews.stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .mapToInt(Review::getScore)
                .average()
                .orElse(0.0);
        for (Restaurant restaurant : restaurantRepository.findAll()) {
            if (restaurant.getId().equals(restaurantId)) {
                restaurant.setRating(BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP));
            }
        }
    }
} 