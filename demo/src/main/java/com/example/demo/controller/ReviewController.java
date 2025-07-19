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

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getById(@PathVariable Long id) {
        return reviewService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ReviewResponseDTO create(@RequestBody ReviewRequestDTO dto) {
        return reviewService.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> update(@PathVariable Long id, @RequestBody ReviewRequestDTO dto) {
        try {
            return ResponseEntity.ok(reviewService.update(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.remove(id);
        return ResponseEntity.noContent().build();
    }
} 