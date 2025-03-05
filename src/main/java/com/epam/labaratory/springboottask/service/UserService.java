package com.epam.labaratory.springboottask.service;


import com.epam.labaratory.springboottask.dto.ActivationRequestDto;
import com.epam.labaratory.springboottask.dto.UserRegisterDto;
import com.epam.labaratory.springboottask.dto.UserRequestDto;
import com.epam.labaratory.springboottask.dto.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRegisterDto userRegisterDto);

    UserResponseDto getUserByUsername(String username);

    UserResponseDto getUserById(Integer id);

    UserResponseDto authenticate(String username, String password);

    Boolean checkIsActive(String username);

    UserResponseDto updateUserPassword(UserRequestDto userRequestDto, String newPassword);

    void updateUserStatus(ActivationRequestDto activationRequestDto);

    UserResponseDto updateUser(UserRequestDto userRequestDto);
}