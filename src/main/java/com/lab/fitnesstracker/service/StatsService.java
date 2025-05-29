package com.lab.fitnesstracker.service;

import com.lab.fitnesstracker.model.User;
import com.lab.fitnesstracker.model.Workout;
import com.lab.fitnesstracker.model.WorkoutType;
import com.lab.fitnesstracker.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsService {
    private final WorkoutRepository workoutRepository;
    private final UserService userService;

    public StatsService(WorkoutRepository workoutRepository, UserService userService) {
        this.workoutRepository = workoutRepository;
        this.userService = userService;
    }

    public List<Map<String, Object>> getWorkoutStatsByType() {
        User user = userService.getCurrentUser();
        List<Workout> workouts = workoutRepository.findByUser(user);

        Map<WorkoutType, List<Workout>> grouped = workouts.stream()
                .collect(Collectors.groupingBy(Workout::getType));

        List<Map<String, Object>> stats = new ArrayList<>();

        for (Map.Entry<WorkoutType, List<Workout>> entry : grouped.entrySet()) {
            WorkoutType type = entry.getKey();
            List<Workout> workoutList = entry.getValue();

            int count = workoutList.size();
            int totalDuration = workoutList.stream().mapToInt(Workout::getDurationMinutes).sum();
            int totalCalories = workoutList.stream().mapToInt(Workout::getCaloriesBurned).sum();

            Map<String, Object> stat = new HashMap<>();
            stat.put("type", type);
            stat.put("count", count);
            stat.put("totalDuration", totalDuration);
            stat.put("totalCalories", totalCalories);

            stats.add(stat);
        }

        return stats;
    }

    public Map<LocalDate, Integer> getCaloriesPerDay() {
        User user = userService.getCurrentUser();
        List<Workout> workouts = workoutRepository.findByUser(user);

        return workouts.stream()
                .collect(Collectors.groupingBy(
                        Workout::getDate,
                        Collectors.summingInt(Workout::getCaloriesBurned)
                ));
    }

    public Map<String, Integer> getCaloriesPerWeek() {
        User user = userService.getCurrentUser();
        List<Workout> workouts = workoutRepository.findByUser(user);

        return workouts.stream()
                .collect(Collectors.groupingBy(
                        w -> {
                            LocalDate date = w.getDate();
                            int week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                            int year = date.get(IsoFields.WEEK_BASED_YEAR);
                            return year + "-W" + week;
                        },
                        Collectors.summingInt(Workout::getCaloriesBurned)
                ));
    }

    public Map<YearMonth, Integer> getCaloriesPerMonth() {
        User user = userService.getCurrentUser();
        List<Workout> workouts = workoutRepository.findByUser(user);

        return workouts.stream()
                .collect(Collectors.groupingBy(
                        w -> YearMonth.from(w.getDate()),
                        Collectors.summingInt(Workout::getCaloriesBurned)
                ));
    }


}
