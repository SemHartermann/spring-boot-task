package com.epam.labaratory.springboottask.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TraineeResponseDto extends BaseDto {
    Date dateOfBirth;
    String address;
    UserResponseDto user;
}