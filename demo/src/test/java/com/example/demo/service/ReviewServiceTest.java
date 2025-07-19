package com.example.demo.service;

import com.example.demo.dto.ReviewRequestDTO;
import com.example.demo.model.CuisineType;
import com.example.demo.model.Restaurant;
import com.example.demo.model.Review;
import com.example.demo.model.Visitor;
import com.example.demo.repository.RestaurantJpaRepository;
import com.example.demo.repository.ReviewJpaRepository;
import com.example.demo.repository.VisitorJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewJpaRepository reviewRepository;
    @Mock
    private RestaurantJpaRepository restaurantRepository;
    @Mock
    private VisitorJpaRepository visitorRepository;
    @InjectMocks
    private ReviewService reviewService;

    private Visitor visitor;
    private Restaurant restaurant;
    private Review review;

    @BeforeEach
    void setUp() {
        visitor = new Visitor(1L, "Test", 20, Visitor.Gender.MALE);
        restaurant = new Restaurant(1L, "Test", "desc", CuisineType.ITALIAN, 100, BigDecimal.ZERO);
        review = new Review(1L, visitor, restaurant, 5, "Good");
    }

    @Test
    void saveReview() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        var dto = new ReviewRequestDTO(1L, 1L, 5, "Good");
        var result = reviewService.save(dto);
        assertThat(result.score()).isEqualTo(5);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void saveReviewVisitorNotFound() {
        when(visitorRepository.findById(2L)).thenReturn(Optional.empty());
        var dto = new ReviewRequestDTO(2L, 1L, 5, "Good");
        assertThrows(IllegalArgumentException.class, () -> reviewService.save(dto));
    }

    @Test
    void saveReviewRestaurantNotFound() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(restaurantRepository.findById(2L)).thenReturn(Optional.empty());
        var dto = new ReviewRequestDTO(1L, 2L, 5, "Good");
        assertThrows(IllegalArgumentException.class, () -> reviewService.save(dto));
    }

    @Test
    void findAllReviews() {
        when(reviewRepository.findAll()).thenReturn(List.of(review));
        var result = reviewService.findAll();
        assertThat(result).hasSize(1);
        verify(reviewRepository).findAll();
    }

    @Test
    void findReviewById() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        var result = reviewService.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().score()).isEqualTo(5);
    }

    @Test
    void updateReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        var dto = new ReviewRequestDTO(1L, 1L, 4, "Updated");
        var result = reviewService.update(1L, dto);
        assertThat(result.score()).isEqualTo(4);
        assertThat(result.comment()).isEqualTo("Updated");
    }

    @Test
    void updateReviewNotFound() {
        when(reviewRepository.findById(2L)).thenReturn(Optional.empty());
        var dto = new ReviewRequestDTO(1L, 1L, 4, "Updated");
        assertThrows(IllegalArgumentException.class, () -> reviewService.update(2L, dto));
    }

    @Test
    void removeReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        doNothing().when(reviewRepository).deleteById(1L);
        reviewService.remove(1L);
        verify(reviewRepository).deleteById(1L);
    }
} 