package com.epam.labaratory.springboottask.dto;

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
