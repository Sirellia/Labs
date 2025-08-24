package com.example.demo;

import com.example.demo.dto.RestaurantRequestDTO;
import com.example.demo.dto.RestaurantResponseDTO;
import com.example.demo.dto.ReviewRequestDTO;
import com.example.demo.dto.VisitorRequestDTO;
import com.example.demo.dto.VisitorResponseDTO;
import com.example.demo.model.CuisineType;
import com.example.demo.model.Visitor;
import com.example.demo.service.RestaurantService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoDataLoader implements CommandLineRunner {
    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @Autowired
    public DemoDataLoader(VisitorService visitorService, RestaurantService restaurantService, ReviewService reviewService) {
        this.visitorService = visitorService;
        this.restaurantService = restaurantService;
        this.reviewService = reviewService;
    }

    @Override
    public void run(String... args) {
        VisitorResponseDTO v1 = visitorService.save(new VisitorRequestDTO("Иван", 25, Visitor.Gender.MALE));
        VisitorResponseDTO v2 = visitorService.save(new VisitorRequestDTO(null, 30, Visitor.Gender.FEMALE));
        VisitorResponseDTO v3 = visitorService.save(new VisitorRequestDTO("Анна", 22, Visitor.Gender.FEMALE));

        RestaurantResponseDTO r1 = restaurantService.save(new RestaurantRequestDTO("Пиццерия Италия", "Настоящая итальянская пицца", CuisineType.ITALIAN, 800));
        RestaurantResponseDTO r2 = restaurantService.save(new RestaurantRequestDTO("Дракон", "Лучший китайский ресторан", CuisineType.CHINESE, 600));
        RestaurantResponseDTO r3 = restaurantService.save(new RestaurantRequestDTO("Европа", "", CuisineType.EUROPEAN, 1000));

        reviewService.save(new ReviewRequestDTO(v1.id(), r1.id(), 5, "Очень вкусно!"));
        reviewService.save(new ReviewRequestDTO(v2.id(), r1.id(), 4, "Пицца хорошая, но долго ждал"));
        reviewService.save(new ReviewRequestDTO(v3.id(), r2.id(), 3, "Средне"));
        reviewService.save(new ReviewRequestDTO(v1.id(), r2.id(), 4, "Вкусная лапша!"));
        reviewService.save(new ReviewRequestDTO(v3.id(), r3.id(), 5, "Отлично!"));
    }
} 
