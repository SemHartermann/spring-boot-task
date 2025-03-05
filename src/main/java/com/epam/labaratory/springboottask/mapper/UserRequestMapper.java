package com.epam.labaratory.springboottask.mapper;



import com.epam.labaratory.springboottask.dto.UserRequestDto;
import com.epam.labaratory.springboottask.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface UserRequestMapper extends Converter<User, UserRequestDto> {
    @Override
    UserRequestDto convert(User user);

    @InheritInverseConfiguration
    @DelegatingConverter
    User invertConvert(UserRequestDto userRequestDto);
}