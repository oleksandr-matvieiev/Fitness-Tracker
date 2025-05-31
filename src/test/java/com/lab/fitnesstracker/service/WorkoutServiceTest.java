package com.lab.fitnesstracker.service;

import com.lab.fitnesstracker.model.User;
import com.lab.fitnesstracker.model.Workout;
import com.lab.fitnesstracker.model.WorkoutType;
import com.lab.fitnesstracker.repository.WorkoutRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    private WorkoutRepository workoutRepository;
    private UserService userService;
    private WorkoutService workoutService;

    @BeforeEach
    void setUp() {
        workoutRepository = mock(WorkoutRepository.class);
        userService = mock(UserService.class);
        workoutService = new WorkoutService(workoutRepository, userService);
    }

    @Test
    void getAllWorkoutsForCurrentUserSuccess() {
        User user = new User();
        List<Workout> workouts = List.of(new Workout(), new Workout());

        when(userService.getCurrentUser()).thenReturn(user);
        when(workoutRepository.findByUser(user)).thenReturn(workouts);

        List<Workout> result = workoutService.getAllWorkoutsForCurrentUser();
        assertEquals(2, result.size());
    }

    @Test
    void getWorkoutByIdSuccess() {
        Workout workout = new Workout();
        workout.setId(1L);

        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));

        Workout result = workoutService.getWorkoutById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getWorkoutByIdNotFound() {
        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> workoutService.getWorkoutById(99L));
    }

    @Test
    void getAllWorkoutsByTypeSuccess() {
        WorkoutType type = WorkoutType.CARDIO;
        List<Workout> workouts = List.of(new Workout(), new Workout());

        when(workoutRepository.findByType(type)).thenReturn(workouts);

        List<Workout> result = workoutService.getAllWorkoutsByType(type);
        assertEquals(2, result.size());
    }

    @Test
    void createWorkoutSuccess() {
        User user = new User();
        Workout request = new Workout();
        request.setType(WorkoutType.SWIMMING);
        request.setDate(LocalDate.now());
        request.setDurationMinutes(45);
        request.setCaloriesBurned(500);

        when(userService.getCurrentUser()).thenReturn(user);

        workoutService.createWorkout(request);

        verify(workoutRepository).save(argThat(saved ->
                saved.getUser().equals(user) &&
                        saved.getType() == WorkoutType.SWIMMING &&
                        saved.getDate().equals(request.getDate()) &&
                        saved.getDurationMinutes().equals(45) &&
                        saved.getCaloriesBurned().equals(500)
        ));
    }

    @Test
    void updateWorkoutSuccess() {
        User user = new User();
        user.setId(1L);

        Workout existing = new Workout();
        existing.setId(1L);
        existing.setUser(user);
        existing.setType(WorkoutType.CARDIO);

        Workout updateRequest = new Workout();
        updateRequest.setType(WorkoutType.SWIMMING);
        updateRequest.setDate(LocalDate.now());
        updateRequest.setDurationMinutes(60);
        updateRequest.setCaloriesBurned(700);

        when(userService.getCurrentUser()).thenReturn(user);
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(existing));

        workoutService.updateWorkout(1L, updateRequest);

        verify(workoutRepository).save(argThat(updated ->
                updated.getType() == WorkoutType.SWIMMING &&
                        updated.getDurationMinutes().equals(60) &&
                        updated.getCaloriesBurned().equals(700)
        ));
    }

    @Test
    void updateWorkoutFailsIfNotOwner() {
        User currentUser = new User();
        currentUser.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Workout existing = new Workout();
        existing.setId(1L);
        existing.setUser(otherUser);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () ->
                workoutService.updateWorkout(1L, new Workout())
        );
    }

    @Test
    void deleteWorkoutSuccess() {
        User user = new User();
        user.setId(1L);

        Workout workout = new Workout();
        workout.setId(1L);
        workout.setUser(user);

        when(userService.getCurrentUser()).thenReturn(user);
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));

        workoutService.deleteWorkout(1L);

        verify(workoutRepository).deleteById(1L);
    }

    @Test
    void deleteWorkoutFailsIfNotOwner() {
        User currentUser = new User();
        currentUser.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Workout workout = new Workout();
        workout.setId(1L);
        workout.setUser(otherUser);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));

        assertThrows(IllegalArgumentException.class, () ->
                workoutService.deleteWorkout(1L)
        );
    }

    @Test
    void deleteWorkoutNotFound() {
        when(workoutRepository.findById(99L)).thenReturn(Optional.empty());
        when(userService.getCurrentUser()).thenReturn(new User());

        assertThrows(EntityNotFoundException.class, () -> workoutService.deleteWorkout(99L));
    }
}
