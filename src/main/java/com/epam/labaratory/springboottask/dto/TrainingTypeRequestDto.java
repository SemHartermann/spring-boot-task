package com.epam.labaratory.springboottask.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainingTypeRequestDto {
    String trainingTypeName;

    @Override
    public String toString() {
        return trainingTypeName;
    }
}