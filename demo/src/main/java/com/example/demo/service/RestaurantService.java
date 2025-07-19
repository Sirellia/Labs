package com.example.demo.service;

import com.example.demo.dto.RestaurantRequestDTO;
import com.example.demo.dto.RestaurantResponseDTO;
import com.example.demo.model.Restaurant;
import com.example.demo.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public RestaurantResponseDTO save(RestaurantRequestDTO dto) {
        Restaurant restaurant = new Restaurant(null, dto.name(), dto.description(), dto.cuisineType(), dto.averageBill(), BigDecimal.ZERO);
        restaurantRepository.save(restaurant);
        return toResponseDTO(restaurant);
    }

    public void remove(Long id) {
        restaurantRepository.findAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .ifPresent(restaurantRepository::remove);
    }

    public List<RestaurantResponseDTO> findAll() {
        return restaurantRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<RestaurantResponseDTO> findById(Long id) {
        return restaurantRepository.findAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .map(this::toResponseDTO);
    }

    public RestaurantResponseDTO update(Long id, RestaurantRequestDTO dto) {
        Optional<Restaurant> optional = restaurantRepository.findAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
        if (optional.isPresent()) {
            Restaurant restaurant = optional.get();
            restaurant.setName(dto.name());
            restaurant.setDescription(dto.description());
            restaurant.setCuisineType(dto.cuisineType());
            restaurant.setAverageBill(dto.averageBill());
            return toResponseDTO(restaurant);
        }
        throw new IllegalArgumentException("Restaurant not found");
    }

    private RestaurantResponseDTO toResponseDTO(Restaurant restaurant) {
        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getCuisineType(),
                restaurant.getAverageBill(),
                restaurant.getRating()
        );
    }
} 