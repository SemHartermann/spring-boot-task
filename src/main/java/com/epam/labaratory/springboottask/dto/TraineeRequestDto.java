package com.epam.labaratory.springboottask.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TraineeRequestDto{
    Date dateOfBirth;
    String address;
    @NotNull
    UserRequestDto user;
}