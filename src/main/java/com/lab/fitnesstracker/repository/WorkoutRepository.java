package com.lab.fitnesstracker.repository;

import com.lab.fitnesstracker.model.User;
import com.lab.fitnesstracker.model.Workout;
import com.lab.fitnesstracker.model.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByType(WorkoutType type);
    List<Workout> findByUser(User user);
}
