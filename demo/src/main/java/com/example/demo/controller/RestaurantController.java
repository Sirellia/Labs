package com.example.demo.controller;

import com.example.demo.dto.RestaurantRequestDTO;
import com.example.demo.dto.RestaurantResponseDTO;
import com.example.demo.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantResponseDTO> getAll() {
        return restaurantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> getById(@PathVariable Long id) {
        return restaurantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RestaurantResponseDTO create(@RequestBody RestaurantRequestDTO dto) {
        return restaurantService.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> update(@PathVariable Long id, @RequestBody RestaurantRequestDTO dto) {
        try {
            return ResponseEntity.ok(restaurantService.update(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.remove(id);
        return ResponseEntity.noContent().build();
    }
} 