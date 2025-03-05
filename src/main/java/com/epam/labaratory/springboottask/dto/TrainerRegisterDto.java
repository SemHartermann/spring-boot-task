package com.epam.labaratory.springboottask.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainerRegisterDto {
    @NotNull
    TrainingTypeRequestDto specialization;
    UserRegisterDto user;
}