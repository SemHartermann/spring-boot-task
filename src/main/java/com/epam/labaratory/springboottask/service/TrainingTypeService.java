package com.epam.labaratory.springboottask.service;

import com.epam.labaratory.springboottask.dto.TrainingTypeResponseDto;

public interface TrainingTypeService {
    TrainingTypeResponseDto getById(Integer id);

    TrainingTypeResponseDto getByName(String trainingTypeName);
}