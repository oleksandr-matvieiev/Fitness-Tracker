package com.lab.fitnesstracker.controller;

import com.lab.fitnesstracker.model.Workout;
import com.lab.fitnesstracker.model.WorkoutType;
import com.lab.fitnesstracker.service.WorkoutService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutControllerTest {

    private final WorkoutService workoutService = mock(WorkoutService.class);
    private final WorkoutController workoutController = new WorkoutController(workoutService);

    @Test
    void getWorkoutsForCurrentUserSuccess() {
        List<Workout> workouts = List.of(new Workout(), new Workout());
        when(workoutService.getAllWorkoutsForCurrentUser()).thenReturn(workouts);

        ResponseEntity<List<Workout>> response = workoutController.getWorkoutsForCurrentUser();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getWorkoutByTypeSuccess() {
        List<Workout> workouts = List.of(new Workout());
        when(workoutService.getAllWorkoutsByType(WorkoutType.CARDIO)).thenReturn(workouts);

        ResponseEntity<List<Workout>> response = workoutController.getWorkoutByType("CARDIO");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getWorkoutByTypeInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            workoutController.getWorkoutByType("INVALID_TYPE");
        });
    }

    @Test
    void getWorkoutByIdSuccess() {
        Workout workout = new Workout();
        workout.setId(1L);
        when(workoutService.getWorkoutById(1L)).thenReturn(workout);

        ResponseEntity<Workout> response = workoutController.getWorkoutById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getWorkoutByIdNotFound() {
        when(workoutService.getWorkoutById(99L)).thenThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> {
            workoutController.getWorkoutById(99L);
        });
    }

    @Test
    void createWorkoutSuccess() {
        Workout request = new Workout();
        doNothing().when(workoutService).createWorkout(request);

        ResponseEntity<Workout> response = workoutController.createWorkout(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(request, response.getBody());
    }

    @Test
    void updateWorkoutSuccess() {
        Workout request = new Workout();
        doNothing().when(workoutService).updateWorkout(1L, request);

        ResponseEntity<Workout> response = workoutController.updateWorkout(1L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(request, response.getBody());
    }

    @Test
    void deleteWorkoutSuccess() {
        doNothing().when(workoutService).deleteWorkout(1L);

        ResponseEntity<Void> response = workoutController.deleteWorkout(1L);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void deleteWorkoutNotFound() {
        doThrow(new EntityNotFoundException()).when(workoutService).deleteWorkout(99L);

        assertThrows(EntityNotFoundException.class, () -> {
            workoutController.deleteWorkout(99L);
        });
    }
}
