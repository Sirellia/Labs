package com.example.demo;

import com.example.demo.model.CuisineType;
import com.example.demo.model.Restaurant;
import com.example.demo.model.Review;
import com.example.demo.model.Visitor;
import com.example.demo.service.RestaurantService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
        Visitor v1 = new Visitor(1L, "Иван", 25, Visitor.Gender.MALE);
        Visitor v2 = new Visitor(2L, null, 30, Visitor.Gender.FEMALE); // аноним
        Visitor v3 = new Visitor(3L, "Анна", 22, Visitor.Gender.FEMALE);
        visitorService.save(v1);
        visitorService.save(v2);
        visitorService.save(v3);

        Restaurant r1 = new Restaurant(1L, "Пиццерия Италия", "Настоящая итальянская пицца", CuisineType.ITALIAN, 800, BigDecimal.ZERO);
        Restaurant r2 = new Restaurant(2L, "Дракон", "Лучший китайский ресторан", CuisineType.CHINESE, 600, BigDecimal.ZERO);
        Restaurant r3 = new Restaurant(3L, "Европа", "", CuisineType.EUROPEAN, 1000, BigDecimal.ZERO);
        restaurantService.save(r1);
        restaurantService.save(r2);
        restaurantService.save(r3);

        reviewService.save(new Review(1L, 1L, 5, "Очень вкусно!"));
        reviewService.save(new Review(2L, 1L, 4, "Пицца хорошая, но долго ждал"));
        reviewService.save(new Review(3L, 2L, 3, "Средне"));
        reviewService.save(new Review(1L, 2L, 4, "Вкусная лапша!"));
        reviewService.save(new Review(3L, 3L, 5, "Отлично!"));

        System.out.println("Все посетители:");
        visitorService.findAll().forEach(System.out::println);
        System.out.println("\nВсе рестораны:");
        restaurantService.findAll().forEach(System.out::println);
        System.out.println("\nВсе отзывы:");
        reviewService.findAll().forEach(System.out::println);
    }
} 