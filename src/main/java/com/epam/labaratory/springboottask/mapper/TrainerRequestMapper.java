package com.epam.labaratory.springboottask.mapper;


import com.epam.labaratory.springboottask.dto.TrainerRequestDto;
import com.epam.labaratory.springboottask.entity.Trainer;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface TrainerRequestMapper extends Converter<Trainer, TrainerRequestDto> {
    @Override
    TrainerRequestDto convert(Trainer trainer);

    @InheritInverseConfiguration
    @DelegatingConverter
    Trainer invertConvert(TrainerRequestDto trainerRequestDto);
}