package com.example.demo.controller;

import com.example.demo.dto.VisitorRequestDTO;
import com.example.demo.dto.VisitorResponseDTO;
import com.example.demo.service.VisitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class VisitorController {
    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping
    public List<VisitorResponseDTO> getAll() {
        return visitorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitorResponseDTO> getById(@PathVariable Long id) {
        return visitorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public VisitorResponseDTO create(@RequestBody VisitorRequestDTO dto) {
        return visitorService.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitorResponseDTO> update(@PathVariable Long id, @RequestBody VisitorRequestDTO dto) {
        try {
            return ResponseEntity.ok(visitorService.update(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        visitorService.remove(id);
        return ResponseEntity.noContent().build();
    }
} 