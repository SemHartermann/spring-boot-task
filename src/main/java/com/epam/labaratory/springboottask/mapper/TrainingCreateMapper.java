package com.epam.labaratory.springboottask.mapper;

import com.epam.labaratory.springboottask.dto.TrainingCreateDto;
import com.epam.labaratory.springboottask.entity.Training;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface TrainingCreateMapper extends Converter<Training, TrainingCreateDto> {
    @Override
    TrainingCreateDto convert(Training training);

    @InheritInverseConfiguration
    @DelegatingConverter
    Training invertConvert(TrainingCreateDto trainingDto);
}