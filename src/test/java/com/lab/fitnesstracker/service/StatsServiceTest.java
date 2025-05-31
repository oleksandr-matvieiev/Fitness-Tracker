package com.lab.fitnesstracker.service;

import com.lab.fitnesstracker.model.User;
import com.lab.fitnesstracker.model.Workout;
import com.lab.fitnesstracker.model.WorkoutType;
import com.lab.fitnesstracker.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatsServiceTest {

    private WorkoutRepository workoutRepository;
    private UserService userService;
    private StatsService statsService;

    private final User user = new User();

    @BeforeEach
    void setUp() {
        workoutRepository = mock(WorkoutRepository.class);
        userService = mock(UserService.class);
        statsService = new StatsService(workoutRepository, userService);

        when(userService.getCurrentUser()).thenReturn(user);
    }

    @Test
    void getWorkoutStatsByTypeReturnsCorrectStats() {
        Workout w1 = new Workout();
        w1.setType(WorkoutType.RUNNING);
        w1.setDurationMinutes(30);
        w1.setCaloriesBurned(250);

        Workout w2 = new Workout();
        w2.setType(WorkoutType.RUNNING);
        w2.setDurationMinutes(30);
        w2.setCaloriesBurned(250);

        List<Workout> workouts = List.of(w1, w2);

        when(workoutRepository.findByUser(user)).thenReturn(workouts);

        List<Map<String, Object>> stats = statsService.getWorkoutStatsByType();

        assertEquals(1, stats.size());

        Map<String, Object> stat = stats.get(0);
        assertEquals(WorkoutType.RUNNING, stat.get("type"));
        assertEquals(2, stat.get("count"));
        assertEquals(60, stat.get("totalDuration"));
        assertEquals(500, stat.get("totalCalories"));
    }

    @Test
    void getCaloriesPerDayReturnsCorrectAggregation() {
        Workout w1 = new Workout();
        w1.setDate(LocalDate.of(2025, 5, 30));
        w1.setCaloriesBurned(300);

        Workout w2 = new Workout();
        w2.setDate(LocalDate.of(2025, 5, 30));
        w2.setCaloriesBurned(200);

        when(workoutRepository.findByUser(user)).thenReturn(List.of(w1, w2));

        Map<LocalDate, Integer> result = statsService.getCaloriesPerDay();

        assertEquals(1, result.size());
        assertEquals(500, result.get(LocalDate.of(2025, 5, 30)));
    }

    @Test
    void getCaloriesPerWeekReturnsCorrectAggregation() {
        Workout w1 = new Workout();
        w1.setDate(LocalDate.of(2025, 5, 30)); // Week 22 of 2025
        w1.setCaloriesBurned(400);

        Workout w2 = new Workout();
        w2.setDate(LocalDate.of(2025, 6, 1)); // Also week 22
        w2.setCaloriesBurned(300);

        when(workoutRepository.findByUser(user)).thenReturn(List.of(w1, w2));

        Map<String, Integer> result = statsService.getCaloriesPerWeek();

        assertEquals(1, result.size());
        String weekKey = "2025-W22";
        assertEquals(700, result.get(weekKey));
    }

    @Test
    void getCaloriesPerMonthReturnsCorrectAggregation() {
        Workout w1 = new Workout();
        w1.setDate(LocalDate.of(2025, 5, 15));
        w1.setCaloriesBurned(700);

        Workout w2 = new Workout();
        w2.setDate(LocalDate.of(2025, 5, 20));
        w2.setCaloriesBurned(800);

        when(workoutRepository.findByUser(user)).thenReturn(List.of(w1, w2));

        Map<YearMonth, Integer> result = statsService.getCaloriesPerMonth();

        assertEquals(1, result.size());
        assertEquals(1500, result.get(YearMonth.of(2025, 5)));
    }
}
