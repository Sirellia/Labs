package com.example.demo.service;

import com.example.demo.dto.VisitorRequestDTO;
import com.example.demo.dto.VisitorResponseDTO;
import com.example.demo.model.Visitor;
import com.example.demo.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitorService {
    private final VisitorRepository visitorRepository;

    @Autowired
    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public VisitorResponseDTO save(VisitorRequestDTO dto) {
        Visitor visitor = new Visitor(null, dto.name(), dto.age(), dto.gender());
        visitorRepository.save(visitor);
        return toResponseDTO(visitor);
    }

    public void remove(Long id) {
        visitorRepository.findAll().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .ifPresent(visitorRepository::remove);
    }

    public List<VisitorResponseDTO> findAll() {
        return visitorRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<VisitorResponseDTO> findById(Long id) {
        return visitorRepository.findAll().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .map(this::toResponseDTO);
    }

    public VisitorResponseDTO update(Long id, VisitorRequestDTO dto) {
        Optional<Visitor> optional = visitorRepository.findAll().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
        if (optional.isPresent()) {
            Visitor visitor = optional.get();
            visitor.setName(dto.name());
            visitor.setAge(dto.age());
            visitor.setGender(dto.gender());
            return toResponseDTO(visitor);
        }
        throw new IllegalArgumentException("Visitor not found");
    }

    private VisitorResponseDTO toResponseDTO(Visitor visitor) {
        return new VisitorResponseDTO(visitor.getId(), visitor.getName(), visitor.getAge(), visitor.getGender());
    }
} 