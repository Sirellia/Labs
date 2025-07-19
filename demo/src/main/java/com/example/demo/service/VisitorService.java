package com.example.demo.service;

import com.example.demo.dto.VisitorRequestDTO;
import com.example.demo.dto.VisitorResponseDTO;
import com.example.demo.model.Visitor;
import com.example.demo.repository.VisitorJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitorService {
    private final VisitorJpaRepository visitorRepository;

    @Autowired
    public VisitorService(VisitorJpaRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public VisitorResponseDTO save(VisitorRequestDTO dto) {
        Visitor visitor = new Visitor(null, dto.name(), dto.age(), dto.gender());
        visitor = visitorRepository.save(visitor);
        return toResponseDTO(visitor);
    }

    public void remove(Long id) {
        visitorRepository.deleteById(id);
    }

    public List<VisitorResponseDTO> findAll() {
        return visitorRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<VisitorResponseDTO> findById(Long id) {
        return visitorRepository.findById(id)
                .map(this::toResponseDTO);
    }

    public VisitorResponseDTO update(Long id, VisitorRequestDTO dto) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found"));
        visitor.setName(dto.name());
        visitor.setAge(dto.age());
        visitor.setGender(dto.gender());
        visitor = visitorRepository.save(visitor);
        return toResponseDTO(visitor);
    }

    private VisitorResponseDTO toResponseDTO(Visitor visitor) {
        return new VisitorResponseDTO(visitor.getId(), visitor.getName(), visitor.getAge(), visitor.getGender());
    }
} 