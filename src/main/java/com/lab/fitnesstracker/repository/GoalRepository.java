package com.lab.fitnesstracker.repository;

import com.lab.fitnesstracker.model.Goal;
import com.lab.fitnesstracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findAllByUser(User user);
}
