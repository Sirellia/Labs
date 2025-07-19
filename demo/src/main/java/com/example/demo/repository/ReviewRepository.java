package com.example.demo.repository;

import com.example.demo.model.Review;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepository {
    private final List<Review> reviews = new ArrayList<>();

    public void save(Review review) {
        reviews.add(review);
    }

    public void remove(Review review) {
        reviews.remove(review);
    }

    public List<Review> findAll() {
        return Collections.unmodifiableList(reviews);
    }

    public Optional<Review> findById(Long visitorId, Long restaurantId) {
        return reviews.stream()
                .filter(r -> r.getVisitorId().equals(visitorId) && r.getRestaurantId().equals(restaurantId))
                .findFirst();
    }
} 
