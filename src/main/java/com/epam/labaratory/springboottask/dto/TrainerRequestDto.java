package com.epam.labaratory.springboottask.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class TrainerRequestDto {
    @NotNull
    TrainingTypeRequestDto specialization;
    @NotNull
    UserRequestDto user;
}