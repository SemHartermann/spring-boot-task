package com.epam.labaratory.springboottask.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TraineeRegisterDto {
    Date dateOfBirth;
    String address;
    UserRegisterDto user;
}
