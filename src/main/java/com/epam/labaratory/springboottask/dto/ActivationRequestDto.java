package com.epam.labaratory.springboottask.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ActivationRequestDto {
    @NotNull
    String username;

    @NotNull
    Boolean isActive;
}