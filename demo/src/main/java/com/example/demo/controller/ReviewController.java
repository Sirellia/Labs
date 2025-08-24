package com.example.demo.controller;

import com.example.demo.dto.ReviewRequestDTO;
import com.example.demo.dto.ReviewResponseDTO;
import com.example.demo.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<ReviewResponseDTO> getAll() {
        return reviewService.findAll();
    }

    @GetMapping("/{visitorId}/{restaurantId}")
    public ResponseEntity<ReviewResponseDTO> getById(@PathVariable Long visitorId, @PathVariable Long restaurantId) {
        return reviewService.findById(visitorId, restaurantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ReviewResponseDTO create(@RequestBody ReviewRequestDTO dto) {
        return reviewService.save(dto);
    }

    @PutMapping("/{visitorId}/{restaurantId}")
    public ResponseEntity<ReviewResponseDTO> update(@PathVariable Long visitorId, @PathVariable Long restaurantId, @RequestBody ReviewRequestDTO dto) {
        try {
            return ResponseEntity.ok(reviewService.update(visitorId, restaurantId, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{visitorId}/{restaurantId}")
    public ResponseEntity<Void> delete(@PathVariable Long visitorId, @PathVariable Long restaurantId) {
        reviewService.remove(visitorId, restaurantId);
        return ResponseEntity.noContent().build();
    }
} 