package com.epam.labaratory.springboottask.mapper;


import com.epam.labaratory.springboottask.dto.TrainingTypeRequestDto;
import com.epam.labaratory.springboottask.dto.TrainingTypeResponseDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface TrainingTypeRequestResponseMapper extends Converter<TrainingTypeResponseDto, TrainingTypeRequestDto> {
    @Override
    TrainingTypeRequestDto convert(TrainingTypeResponseDto trainingTypeResponseDto);

    @InheritInverseConfiguration
    @DelegatingConverter
    TrainingTypeResponseDto invertConvert(TrainingTypeRequestDto trainingTypeRequestDto);
}