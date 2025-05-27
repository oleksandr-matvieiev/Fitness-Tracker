package com.lab.fitnesstracker.service;

import com.lab.fitnesstracker.model.User;
import com.lab.fitnesstracker.model.Workout;
import com.lab.fitnesstracker.model.WorkoutType;
import com.lab.fitnesstracker.repository.WorkoutRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserService userService;

    public WorkoutService(WorkoutRepository workoutRepository, UserService userService) {
        this.workoutRepository = workoutRepository;
        this.userService = userService;
    }

    public List<Workout> getAllWorkoutsForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return workoutRepository.findByUser(currentUser);
    }

    public Workout getWorkoutById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout with ID " + id + " not found"));
    }

    public List<Workout> getAllWorkoutsByType(WorkoutType type) {
        return workoutRepository.findByType(type);
    }

    public void createWorkout(Workout request) {
        User currentUser = userService.getCurrentUser();

        Workout workout = new Workout();
        workout.setUser(currentUser);
        workout.setType(request.getType());
        workout.setDate(request.getDate());
        workout.setDurationMinutes(request.getDurationMinutes());
        workout.setCaloriesBurned(request.getCaloriesBurned());

        workoutRepository.save(workout);
    }

    public void updateWorkout(Long id, Workout workout) {
        User currentUser = userService.getCurrentUser();
        Workout toUpdate = workoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout with ID " + id + " not found"));

        if (!toUpdate.getUser().equals(currentUser)) {
            throw new IllegalArgumentException("Wrong user");
        }

        if (workout.getType() != null) {
            toUpdate.setType(workout.getType());
        }
        if (workout.getDate() != null) {
            toUpdate.setDate(workout.getDate());
        }
        if (workout.getDurationMinutes() != null) {
            toUpdate.setDurationMinutes(workout.getDurationMinutes());
        }
        if (workout.getCaloriesBurned() != null) {
            toUpdate.setCaloriesBurned(workout.getCaloriesBurned());
        }

        workoutRepository.save(toUpdate);
    }


    public void deleteWorkout(Long id) {
        User currentUser = userService.getCurrentUser();
        Workout workoutToDelete = workoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout with ID " + id + " not found"));

        if (!workoutToDelete.getUser().equals(currentUser)) {
            throw new IllegalArgumentException("Workout does not belong to the current user");
        }

        workoutRepository.deleteById(id);
    }
}
