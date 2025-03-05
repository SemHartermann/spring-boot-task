package com.epam.labaratory.springboottask.mapper;


import com.epam.labaratory.springboottask.dto.TraineeRegisterDto;
import com.epam.labaratory.springboottask.dto.TraineeResponseDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface TraineeRegisterResponseMapper extends Converter<TraineeRegisterDto, TraineeResponseDto> {
    @Override
    TraineeResponseDto convert(TraineeRegisterDto traineeRegisterDto);

    @InheritInverseConfiguration
    @DelegatingConverter
    TraineeRegisterDto invertConvert(TraineeResponseDto traineeResponseDto);
}