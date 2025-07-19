package com.example.demo.dto;

import com.example.demo.model.Visitor;

public record VisitorResponseDTO(Long id, String name, int age, Visitor.Gender gender) {} 