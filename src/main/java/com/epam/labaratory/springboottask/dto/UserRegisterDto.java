package com.epam.labaratory.springboottask.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class UserRegisterDto {
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
}
