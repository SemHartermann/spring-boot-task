package com.epam.labaratory.springboottask.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class TrainingCreateDto {
    String traineeUsername;
    String trainerUsername;
    @NotBlank
    String trainingName;
    TrainingTypeRequestDto trainingType;
    @NotNull
    Date trainingDate;
    @NotNull
    Integer trainingDuration;
}