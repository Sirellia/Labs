package com.example.demo.controller;

import com.example.demo.dto.ReviewRequestDTO;
import com.example.demo.dto.ReviewResponseDTO;
import com.example.demo.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReviewService reviewService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllReviews() throws Exception {
        Mockito.when(reviewService.findAll()).thenReturn(List.of(new ReviewResponseDTO(1L, 1L, 5, "Good")));
        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorId").value(1L));
    }

    @Test
    void getReviewById() throws Exception {
        Mockito.when(reviewService.findById(1L)).thenReturn(Optional.of(new ReviewResponseDTO(1L, 1L, 5, "Good")));
        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorId").value(1L));
    }

    @Test
    void createReview() throws Exception {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 1L, 5, "Good");
        ReviewResponseDTO response = new ReviewResponseDTO(1L, 1L, 5, "Good");
        Mockito.when(reviewService.save(any())).thenReturn(response);
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorId").value(1L));
    }

    @Test
    void updateReview() throws Exception {
        ReviewRequestDTO dto = new ReviewRequestDTO(1L, 1L, 4, "Updated");
        ReviewResponseDTO response = new ReviewResponseDTO(1L, 1L, 4, "Updated");
        Mockito.when(reviewService.update(eq(1L), any())).thenReturn(response);
        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(4));
    }

    @Test
    void deleteReview() throws Exception {
        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }
} 