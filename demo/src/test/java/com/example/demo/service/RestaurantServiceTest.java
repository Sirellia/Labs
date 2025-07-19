package com.example.demo.service;

import com.example.demo.dto.RestaurantRequestDTO;
import com.example.demo.model.CuisineType;
import com.example.demo.model.Restaurant;
import com.example.demo.repository.RestaurantJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {
    @Mock
    private RestaurantJpaRepository restaurantRepository;
    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant(1L, "Test", "desc", CuisineType.ITALIAN, 100, BigDecimal.ZERO);
    }

    @Test
    void saveRestaurant() {
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        var dto = new RestaurantRequestDTO("Test", "desc", CuisineType.ITALIAN, 100);
        var result = restaurantService.save(dto);
        assertThat(result.id()).isEqualTo(1L);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void findAllRestaurants() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));
        var result = restaurantService.findAll();
        assertThat(result).hasSize(1);
        verify(restaurantRepository).findAll();
    }

    @Test
    void findRestaurantById() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        var result = restaurantService.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1L);
    }

    @Test
    void updateRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        var dto = new RestaurantRequestDTO("Updated", "desc2", CuisineType.CHINESE, 200);
        var result = restaurantService.update(1L, dto);
        assertThat(result.name()).isEqualTo("Updated");
        assertThat(result.cuisineType()).isEqualTo(CuisineType.CHINESE);
        assertThat(result.averageBill()).isEqualTo(200);
    }

    @Test
    void updateRestaurantNotFound() {
        when(restaurantRepository.findById(2L)).thenReturn(Optional.empty());
        var dto = new RestaurantRequestDTO("Updated", "desc2", CuisineType.CHINESE, 200);
        assertThrows(IllegalArgumentException.class, () -> restaurantService.update(2L, dto));
    }

    @Test
    void removeRestaurant() {
        doNothing().when(restaurantRepository).deleteById(1L);
        restaurantService.remove(1L);
        verify(restaurantRepository).deleteById(1L);
    }
} 