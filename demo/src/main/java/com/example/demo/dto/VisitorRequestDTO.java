package com.example.demo.dto;

import com.example.demo.model.Visitor;

public record VisitorRequestDTO(String name, int age, Visitor.Gender gender) {} 