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
public class TrainingDto extends BaseDto {
    TraineeResponseDto trainee;
    TrainerResponseDto trainer;
    @NotBlank
    String trainingName;
    TrainingTypeDto trainingType;
    @NotNull
    Date trainingDate;
    @NotNull
    Integer trainingDuration;
}