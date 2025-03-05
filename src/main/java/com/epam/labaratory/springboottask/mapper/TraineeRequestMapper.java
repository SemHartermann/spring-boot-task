package com.epam.labaratory.springboottask.mapper;



import com.epam.labaratory.springboottask.dto.TraineeRequestDto;
import com.epam.labaratory.springboottask.entity.Trainee;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface TraineeRequestMapper extends Converter<Trainee, TraineeRequestDto> {
    @Override
    TraineeRequestDto convert(Trainee trainee);

    @InheritInverseConfiguration
    @DelegatingConverter
    Trainee invertConvert(TraineeRequestDto traineeRequestDto);
}