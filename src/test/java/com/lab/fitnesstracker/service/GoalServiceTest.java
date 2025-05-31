package com.lab.fitnesstracker.service;

import com.lab.fitnesstracker.model.Goal;
import com.lab.fitnesstracker.model.User;
import com.lab.fitnesstracker.repository.GoalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoalServiceTest {

    private final GoalRepository goalRepository = mock(GoalRepository.class);
    private final UserService userService = mock(UserService.class);
    private final GoalService goalService = new GoalService(goalRepository, userService);

    @Test
    void getGoalsForCurrentUserSuccess() {
        User user = new User();
        when(userService.getCurrentUser()).thenReturn(user);

        List<Goal> goals = List.of(new Goal(), new Goal());
        when(goalRepository.findAllByUser(user)).thenReturn(goals);

        List<Goal> result = goalService.getGoalsForCurrentUser();

        assertEquals(2, result.size());
    }

    @Test
    void getGoalByIdSuccess() {
        Goal goal = new Goal();
        goal.setId(1L);

        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        Goal result = goalService.getGoalById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getGoalByIdNotFound() {
        when(goalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> goalService.getGoalById(99L));
    }

    @Test
    void createGoalSuccess() {
        User user = new User();
        Goal input = new Goal();
        input.setDescription("Run");
        input.setTargetValue(10);
        input.setCurrentValue(2);
        input.setStartDate(LocalDate.now());
        input.setEndDate(LocalDate.now().plusDays(30));
        input.setIsAchieved(false);

        when(userService.getCurrentUser()).thenReturn(user);

        goalService.createGoal(input);

        verify(goalRepository).save(argThat(saved ->
                saved.getUser().equals(user) &&
                        saved.getDescription().equals("Run") &&
                        saved.getTargetValue().equals(10) &&
                        saved.getCurrentValue().equals(2) &&
                        saved.getStartDate().equals(input.getStartDate()) &&
                        saved.getEndDate().equals(input.getEndDate()) &&
                        !saved.getIsAchieved()
        ));
    }

    @Test
    void updateGoalSuccess() {
        User user = new User();
        Goal existing = new Goal();
        existing.setId(1L);
        existing.setUser(user);

        Goal update = new Goal();
        update.setDescription("New desc");
        update.setTargetValue(100);
        update.setCurrentValue(10);
        update.setStartDate(LocalDate.of(2024, 1, 1));
        update.setEndDate(LocalDate.of(2025, 1, 1));
        update.setIsAchieved(true);

        when(userService.getCurrentUser()).thenReturn(user);
        when(goalRepository.findById(1L)).thenReturn(Optional.of(existing));

        goalService.updateGoal(1L, update);

        assertEquals("New desc", existing.getDescription());
        assertEquals(100, existing.getTargetValue());
        assertEquals(10, existing.getCurrentValue());
        assertEquals(LocalDate.of(2024, 1, 1), existing.getStartDate());
        assertEquals(LocalDate.of(2025, 1, 1), existing.getEndDate());
        assertTrue(existing.getIsAchieved());

        verify(goalRepository).save(existing);
    }

    @Test
    void updateGoalNotFound() {
        Goal update = new Goal();
        when(goalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> goalService.updateGoal(99L, update));
    }

    @Test
    void updateGoalForbidden() {
        Goal existing = new Goal();
        existing.setUser(new User());

        User anotherUser = new User();
        when(userService.getCurrentUser()).thenReturn(anotherUser);
        when(goalRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> goalService.updateGoal(1L, new Goal()));
    }

    @Test
    void deleteGoalSuccess() {
        User user = new User();
        Goal goal = new Goal();
        goal.setUser(user);

        when(userService.getCurrentUser()).thenReturn(user);
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        goalService.deleteGoal(1L);

        verify(goalRepository).delete(goal);
    }

    @Test
    void deleteGoalNotFound() {
        when(goalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> goalService.deleteGoal(99L));
    }

    @Test
    void deleteGoalForbidden() {
        Goal goal = new Goal();
        goal.setUser(new User());

        User anotherUser = new User();
        when(userService.getCurrentUser()).thenReturn(anotherUser);
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        assertThrows(IllegalArgumentException.class, () -> goalService.deleteGoal(1L));
    }
}
