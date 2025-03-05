package com.epam.labaratory.springboottask.service.impl;

import com.epam.labaratory.springboottask.dto.TrainingTypeResponseDto;
import com.epam.labaratory.springboottask.entity.TrainingType;
import com.epam.labaratory.springboottask.repository.TrainingTypeRepository;
import com.epam.labaratory.springboottask.service.TrainingTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrainingTypeServiceImpl implements TrainingTypeService {

    TrainingTypeRepository trainingTypeRepository;
    ConversionService conversionService;

    @Override
    public TrainingTypeResponseDto getById(Integer id) {
        TrainingType trainingType = trainingTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Training type not found by id: " + id));
        return conversionService.convert(trainingType, TrainingTypeResponseDto.class);
    }

    @Override
    public TrainingTypeResponseDto getByName(String trainingTypeName) {
        TrainingType trainingType = trainingTypeRepository.findByTrainingTypeName(trainingTypeName)
                .orElseThrow(() -> new IllegalArgumentException("Training type not found by name: " + trainingTypeName));
        return conversionService.convert(trainingType, TrainingTypeResponseDto.class);
    }
}