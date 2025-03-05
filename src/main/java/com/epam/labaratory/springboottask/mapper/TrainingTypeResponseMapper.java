package com.epam.labaratory.springboottask.mapper;


import com.epam.labaratory.springboottask.dto.TrainingTypeResponseDto;
import com.epam.labaratory.springboottask.entity.TrainingType;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface TrainingTypeResponseMapper extends Converter<TrainingType, TrainingTypeResponseDto> {
    @Override
    TrainingTypeResponseDto convert(TrainingType trainingType);

    @InheritInverseConfiguration
    @DelegatingConverter
    TrainingType invertConvert(TrainingTypeResponseDto trainingTypeResponseDto);
}