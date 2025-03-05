package com.epam.labaratory.springboottask.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class UserResponseDto extends BaseDto {
    String firstName;
    String lastName;
    String username;
    Boolean isActive;
    String password;
}