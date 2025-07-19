package com.example.demo.repository;

import com.example.demo.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorJpaRepository extends JpaRepository<Visitor, Long> {
} 