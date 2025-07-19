package com.example.demo.controller;

import com.example.demo.dto.RestaurantRequestDTO;
import com.example.demo.dto.RestaurantResponseDTO;
import com.example.demo.model.CuisineType;
import com.example.demo.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RestaurantService restaurantService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllRestaurants() throws Exception {
        Mockito.when(restaurantService.findAll()).thenReturn(List.of(new RestaurantResponseDTO(1L, "Test", "desc", CuisineType.ITALIAN, 100, BigDecimal.ZERO)));
        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getRestaurantById() throws Exception {
        Mockito.when(restaurantService.findById(1L)).thenReturn(Optional.of(new RestaurantResponseDTO(1L, "Test", "desc", CuisineType.ITALIAN, 100, BigDecimal.ZERO)));
        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createRestaurant() throws Exception {
        RestaurantRequestDTO dto = new RestaurantRequestDTO("Test", "desc", CuisineType.ITALIAN, 100);
        RestaurantResponseDTO response = new RestaurantResponseDTO(1L, "Test", "desc", CuisineType.ITALIAN, 100, BigDecimal.ZERO);
        Mockito.when(restaurantService.save(any())).thenReturn(response);
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateRestaurant() throws Exception {
        RestaurantRequestDTO dto = new RestaurantRequestDTO("Updated", "desc2", CuisineType.CHINESE, 200);
        RestaurantResponseDTO response = new RestaurantResponseDTO(1L, "Updated", "desc2", CuisineType.CHINESE, 200, BigDecimal.ZERO);
        Mockito.when(restaurantService.update(eq(1L), any())).thenReturn(response);
        mockMvc.perform(put("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void deleteRestaurant() throws Exception {
        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isNoContent());
    }
} 