package com.epam.labaratory.springboottask.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "training_types")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainingType extends BaseEntity {
    @Column(name = "training_type_name", nullable = false, unique = true)
    String trainingTypeName;
}