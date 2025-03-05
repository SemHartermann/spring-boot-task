package com.epam.labaratory.springboottask.mapper;


import com.epam.labaratory.springboottask.dto.UserRegisterDto;
import com.epam.labaratory.springboottask.dto.UserResponseDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface UserRegisterResponseMapper extends Converter<UserRegisterDto, UserResponseDto> {
    @Override
    UserResponseDto convert(UserRegisterDto userRegisterDto);

    @InheritInverseConfiguration
    @DelegatingConverter
    UserRegisterDto invertConvert(UserResponseDto userResponseDto);
}