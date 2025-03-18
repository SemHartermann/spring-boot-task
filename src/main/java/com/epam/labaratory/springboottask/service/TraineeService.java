package com.epam.labaratory.springboottask.service;

import com.epam.labaratory.springboottask.dto.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Date;
import java.util.List;

public interface TraineeService {
    RegisterResponseDto<TraineeResponseDto> createTrainee(TraineeRegisterDto traineeRegisterDto);

    TraineeResponseDto getTraineeByUsername(String username);

    TraineeResponseDto getTraineeById(Integer id);

    TraineeResponseDto updateTraineeProfile(TraineeRequestDto traineeRequestDto);

    TraineeResponseDto updateTraineePassword(String username, String newPassword) throws UserPrincipalNotFoundException;

    void deleteTraineeProfileByUsername(String username);

    List<TrainingResponseDto> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType);

    List<TrainerResponseDto> getUnassignedTrainers(String username);

    TraineeResponseDto updateTraineeTrainersList(String traineeUsername, List<String> trainerUsernames);

    TraineeResponseDto authenticate(String username, String password);
}