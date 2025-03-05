package com.epam.labaratory.springboottask.mapper;



import com.epam.labaratory.springboottask.dto.TrainerRequestDto;
import com.epam.labaratory.springboottask.dto.TrainerResponseDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface TrainerRequestResponseMapper extends Converter<TrainerRequestDto, TrainerResponseDto> {
    @Override
    TrainerResponseDto convert(TrainerRequestDto trainerRequestDto);

    @InheritInverseConfiguration
    @DelegatingConverter
    TrainerRequestDto invertConvert(TrainerResponseDto trainerResponseDto);
}