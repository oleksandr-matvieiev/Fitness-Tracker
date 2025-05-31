package com.lab.fitnesstracker.controller;

import com.lab.fitnesstracker.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatsControllerTest {

    private final StatsService statsService = mock(StatsService.class);
    private final StatsController statsController = new StatsController(statsService);

    @Test
    void getWorkoutStatsByTypeSuccess() {
        List<Map<String, Object>> expected = new ArrayList<>();
        Map<String, Object> stat = new HashMap<>();
        stat.put("type", "RUNNING");
        stat.put("count", 2);
        stat.put("totalDuration", 60);
        stat.put("totalCalories", 500);
        expected.add(stat);

        when(statsService.getWorkoutStatsByType()).thenReturn(expected);

        ResponseEntity<List<Map<String, Object>>> response = statsController.getWorkoutStatsByType();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getCaloriesPerDaySuccess() {
        Map<LocalDate, Integer> expected = Map.of(LocalDate.of(2025, 5, 30), 300);

        when(statsService.getCaloriesPerDay()).thenReturn(expected);

        ResponseEntity<Map<LocalDate, Integer>> response = statsController.getCaloriesPerDay();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getCaloriesPerWeekSuccess() {
        Map<String, Integer> expected = Map.of("2025-W22", 700);

        when(statsService.getCaloriesPerWeek()).thenReturn(expected);

        ResponseEntity<Map<String, Integer>> response = statsController.getCaloriesPerWeek();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getCaloriesPerMonthSuccess() {
        Map<YearMonth, Integer> expected = Map.of(YearMonth.of(2025, 5), 1500);

        when(statsService.getCaloriesPerMonth()).thenReturn(expected);

        ResponseEntity<Map<YearMonth, Integer>> response = statsController.getCaloriesPerMonth();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected, response.getBody());
    }
}
