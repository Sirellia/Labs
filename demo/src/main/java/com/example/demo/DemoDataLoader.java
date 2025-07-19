package com.example.demo;

import com.example.demo.dto.RestaurantRequestDTO;
import com.example.demo.dto.ReviewRequestDTO;
import com.example.demo.dto.VisitorRequestDTO;
import com.example.demo.model.CuisineType;
import com.example.demo.model.Visitor;
import com.example.demo.service.RestaurantService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        visitorService.save(new VisitorRequestDTO("Иван", 25, Visitor.Gender.MALE));
        visitorService.save(new VisitorRequestDTO(null, 30, Visitor.Gender.FEMALE));
        visitorService.save(new VisitorRequestDTO("Анна", 22, Visitor.Gender.FEMALE));

        restaurantService.save(new RestaurantRequestDTO("Пиццерия Италия", "Настоящая итальянская пицца", CuisineType.ITALIAN, 800));
        restaurantService.save(new RestaurantRequestDTO("Дракон", "Лучший китайский ресторан", CuisineType.CHINESE, 600));
        restaurantService.save(new RestaurantRequestDTO("Европа", "", CuisineType.EUROPEAN, 1000));

        reviewService.save(new ReviewRequestDTO(1L, 1L, 5, "Очень вкусно!"));
        reviewService.save(new ReviewRequestDTO(2L, 1L, 4, "Пицца хорошая, но долго ждал"));
        reviewService.save(new ReviewRequestDTO(3L, 2L, 3, "Средне"));
        reviewService.save(new ReviewRequestDTO(1L, 2L, 4, "Вкусная лапша!"));
        reviewService.save(new ReviewRequestDTO(3L, 3L, 5, "Отлично!"));
    }
} 