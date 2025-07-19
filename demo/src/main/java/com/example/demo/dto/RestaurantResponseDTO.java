package com.example.demo.dto;

import com.example.demo.model.CuisineType;
import java.math.BigDecimal;

public record RestaurantResponseDTO(Long id, String name, String description, CuisineType cuisineType, int averageBill, BigDecimal rating) {} 