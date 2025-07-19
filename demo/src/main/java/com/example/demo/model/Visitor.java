package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visitor {
    private Long id;
    private String name; // Необязательное
    private int age;
    private Gender gender;

    public enum Gender {
        MALE, FEMALE, OTHER
    }
} 