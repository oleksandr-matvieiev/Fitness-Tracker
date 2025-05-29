package com.lab.fitnesstracker.controller;

import com.lab.fitnesstracker.model.Goal;
import com.lab.fitnesstracker.model.Workout;
import com.lab.fitnesstracker.model.WorkoutType;
import com.lab.fitnesstracker.service.GoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {
    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public ResponseEntity<List<Goal>> getGoalsForCurrentUser() {
        List<Goal> goals = goalService.getGoalsForCurrentUser();
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goal> getGoalById(@PathVariable Long id) {
        Goal goal = goalService.getGoalById(id);
        return ResponseEntity.ok(goal);
    }

    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody Goal goal) {
        goalService.createGoal(goal);
        return ResponseEntity.ok(goal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Goal> updateGoal(@PathVariable Long id, @RequestBody Goal goal) {
        goalService.updateGoal(id, goal);
        return ResponseEntity.ok(goal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}
