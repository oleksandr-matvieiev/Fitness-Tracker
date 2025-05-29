package com.lab.fitnesstracker.service;

import com.lab.fitnesstracker.model.Goal;
import com.lab.fitnesstracker.model.User;
import com.lab.fitnesstracker.repository.GoalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalService {
    private final GoalRepository goalRepository;
    private final UserService userService;


    public GoalService(GoalRepository goalRepository, UserService userService) {
        this.goalRepository = goalRepository;
        this.userService = userService;
    }

    public List<Goal> getGoalsForCurrentUser() {
        return goalRepository.findAllByUser(userService.getCurrentUser());
    }

    public Goal getGoalById(Long id) {
        return goalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));
    }

    public void createGoal(Goal goal) {
        User user = userService.getCurrentUser();

        Goal newGoal = new Goal();
        newGoal.setUser(user);
        newGoal.setDescription(goal.getDescription());
        newGoal.setTargetValue(goal.getTargetValue());
        newGoal.setCurrentValue(goal.getCurrentValue());
        newGoal.setStartDate(goal.getStartDate());
        newGoal.setEndDate(goal.getEndDate());
        newGoal.setIsAchieved(goal.getIsAchieved());

        goalRepository.save(newGoal);
    }

    public void updateGoal(Long id, Goal goal) {
        User user = userService.getCurrentUser();
        Goal oldGoal = goalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));

        if (!oldGoal.getUser().equals(user)) {
            throw new IllegalArgumentException("You are not allowed to update this goal");
        }

        if (goal.getDescription() != null) {
            oldGoal.setDescription(goal.getDescription());
        }
        if (goal.getTargetValue() != null) {
            oldGoal.setTargetValue(goal.getTargetValue());
        }
        if (goal.getCurrentValue() != null) {
            oldGoal.setCurrentValue(goal.getCurrentValue());
        }
        if (goal.getStartDate() != null) {
            oldGoal.setStartDate(goal.getStartDate());
        }
        if (goal.getEndDate() != null) {
            oldGoal.setEndDate(goal.getEndDate());
        }
        if (goal.getIsAchieved() != null) {
            oldGoal.setIsAchieved(goal.getIsAchieved());
        }
        goalRepository.save(oldGoal);
    }

    public void deleteGoal(Long id) {
        User user = userService.getCurrentUser();
        Goal goalToDelete = goalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));
        if (!goalToDelete.getUser().equals(user)) {
            throw new IllegalArgumentException("You are not allowed to delete this goal");
        }
        goalRepository.delete(goalToDelete);

    }

}
