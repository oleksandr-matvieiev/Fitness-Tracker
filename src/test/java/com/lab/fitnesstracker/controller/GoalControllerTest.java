package com.lab.fitnesstracker.controller;

import com.lab.fitnesstracker.model.Goal;
import com.lab.fitnesstracker.service.GoalService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoalControllerTest {

    private final GoalService goalService = mock(GoalService.class);
    private final GoalController goalController = new GoalController(goalService);

    @Test
    void getGoalsForCurrentUserSuccess() {
        List<Goal> goals = List.of(new Goal(), new Goal());
        when(goalService.getGoalsForCurrentUser()).thenReturn(goals);

        ResponseEntity<List<Goal>> response = goalController.getGoalsForCurrentUser();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getGoalByIdSuccess() {
        Goal goal = new Goal();
        goal.setId(1L);
        when(goalService.getGoalById(1L)).thenReturn(goal);

        ResponseEntity<Goal> response = goalController.getGoalById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getGoalByIdNotFound() {
        when(goalService.getGoalById(99L)).thenThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> {
            goalController.getGoalById(99L);
        });
    }

    @Test
    void createGoalSuccess() {
        Goal request = new Goal();
        doNothing().when(goalService).createGoal(request);

        ResponseEntity<Goal> response = goalController.createGoal(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(request, response.getBody());
    }

    @Test
    void updateGoalSuccess() {
        Goal request = new Goal();
        doNothing().when(goalService).updateGoal(1L, request);

        ResponseEntity<Goal> response = goalController.updateGoal(1L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(request, response.getBody());
    }

    @Test
    void updateGoalNotFound() {
        Goal request = new Goal();
        doThrow(new EntityNotFoundException()).when(goalService).updateGoal(99L, request);

        assertThrows(EntityNotFoundException.class, () -> {
            goalController.updateGoal(99L, request);
        });
    }

    @Test
    void updateGoalForbidden() {
        Goal request = new Goal();
        doThrow(new IllegalArgumentException("You are not allowed")).when(goalService).updateGoal(1L, request);

        assertThrows(IllegalArgumentException.class, () -> {
            goalController.updateGoal(1L, request);
        });
    }

    @Test
    void deleteGoalSuccess() {
        doNothing().when(goalService).deleteGoal(1L);

        ResponseEntity<Void> response = goalController.deleteGoal(1L);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void deleteGoalNotFound() {
        doThrow(new EntityNotFoundException()).when(goalService).deleteGoal(99L);

        assertThrows(EntityNotFoundException.class, () -> {
            goalController.deleteGoal(99L);
        });
    }

    @Test
    void deleteGoalForbidden() {
        doThrow(new IllegalArgumentException("You are not allowed")).when(goalService).deleteGoal(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            goalController.deleteGoal(1L);
        });
    }
}
