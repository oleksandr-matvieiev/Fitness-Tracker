package com.lab.fitnesstracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Integer targetValue;

    private Integer currentValue;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isAchieved;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
