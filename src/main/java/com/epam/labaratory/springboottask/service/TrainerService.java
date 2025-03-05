package com.epam.labaratory.springboottask.service;

import com.epam.labaratory.springboottask.dto.TrainerRegisterDto;
import com.epam.labaratory.springboottask.dto.TrainerRequestDto;
import com.epam.labaratory.springboottask.dto.TrainerResponseDto;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface TrainerService {
    TrainerResponseDto createTrainer(TrainerRegisterDto trainerRegisterDto);

    TrainerResponseDto getTrainerByUsername(String username);

    TrainerResponseDto getTrainerById(Integer id);

    TrainerResponseDto updateTrainerProfile(TrainerRequestDto trainerRequestDto);

    TrainerResponseDto updateTrainerPassword(String username, String newPassword) throws UserPrincipalNotFoundException;

    TrainerResponseDto authenticate(String username, String password);
}