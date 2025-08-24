package com.example.demo.dto;

import com.example.demo.model.CuisineType;

public record RestaurantRequestDTO(String name, String description, CuisineType cuisineType, int averageBill) {} 