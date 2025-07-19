package com.example.demo.dto;

public record ReviewRequestDTO(Long visitorId, Long restaurantId, int score, String comment) {} 