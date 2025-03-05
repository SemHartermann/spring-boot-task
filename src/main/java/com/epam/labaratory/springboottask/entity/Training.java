package com.epam.labaratory.springboottask.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "trainings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Training extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "trainee_id")
    Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    Trainer trainer;

    @Column(name = "training_name", nullable = false)
    String trainingName;

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    TrainingType trainingType;

    @Column(name = "training_date", nullable = false)
    Date trainingDate;

    @Column(name = "training_duration", nullable = false)
    Integer trainingDuration;
}