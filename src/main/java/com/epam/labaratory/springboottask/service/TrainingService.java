package com.epam.labaratory.springboottask.service;

import com.epam.labaratory.springboottask.dto.TrainingCreateDto;
import com.epam.labaratory.springboottask.dto.TrainingResponseDto;

import java.util.Date;
import java.util.List;

public interface TrainingService {
    TrainingResponseDto createTraining(TrainingCreateDto trainingDto);

    TrainingResponseDto getTrainingById(Integer id);

    List<TrainingResponseDto> getTraineeTrainings(String username, Date fromDate, Date toDate);

    List<TrainingResponseDto> getTrainerTrainings(String username, Date fromDate, Date toDate);
}