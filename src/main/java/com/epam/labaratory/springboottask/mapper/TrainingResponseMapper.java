package com.epam.labaratory.springboottask.mapper;

import com.epam.labaratory.springboottask.dto.TrainingResponseDto;
import com.epam.labaratory.springboottask.entity.Training;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface TrainingResponseMapper extends Converter<Training, TrainingResponseDto> {
    @Override
    TrainingResponseDto convert(Training training);

    @InheritInverseConfiguration
    @DelegatingConverter
    Training invertConvert(TrainingResponseDto trainingDto);
}