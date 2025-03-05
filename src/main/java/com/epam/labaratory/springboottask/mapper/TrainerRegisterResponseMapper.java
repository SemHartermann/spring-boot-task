package com.epam.labaratory.springboottask.mapper;



import com.epam.labaratory.springboottask.dto.TrainerRegisterDto;
import com.epam.labaratory.springboottask.dto.TrainerResponseDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface TrainerRegisterResponseMapper extends Converter<TrainerRegisterDto, TrainerResponseDto> {
    @Override
    TrainerResponseDto convert(TrainerRegisterDto trainerRegisterDto);

    @InheritInverseConfiguration
    @DelegatingConverter
    TrainerRegisterDto invertConvert(TrainerResponseDto trainerResponseDto);
}