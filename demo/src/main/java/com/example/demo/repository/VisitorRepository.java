package com.example.demo.repository;

import com.example.demo.model.Visitor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class VisitorRepository {
    private final List<Visitor> visitors = new ArrayList<>();

    public void save(Visitor visitor) {
        visitors.add(visitor);
    }

    public void remove(Visitor visitor) {
        visitors.remove(visitor);
    }

    public List<Visitor> findAll() {
        return Collections.unmodifiableList(visitors);
    }
} 