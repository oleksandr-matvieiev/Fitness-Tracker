package com.lab.fitnesstracker.controller;

import com.lab.fitnesstracker.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/workouts/by-type")
    public ResponseEntity<List<Map<String, Object>>> getWorkoutStatsByType() {
        return ResponseEntity.ok(statsService.getWorkoutStatsByType());
    }

    @GetMapping("/progress/calories/days")
    public ResponseEntity<Map<LocalDate, Integer>> getCaloriesPerDay() {
        return ResponseEntity.ok(statsService.getCaloriesPerDay());
    }

    @GetMapping("/progress/calories/weeks")
    public ResponseEntity<Map<String, Integer>> getCaloriesPerWeek() {
        return ResponseEntity.ok(statsService.getCaloriesPerWeek());
    }

    @GetMapping("/progress/calories/months")
    public ResponseEntity<Map<YearMonth, Integer>> getCaloriesPerMonth() {
        return ResponseEntity.ok(statsService.getCaloriesPerMonth());
    }
}
