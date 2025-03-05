package com.epam.labaratory.springboottask.service;

import com.epam.labaratory.springboottask.dto.TrainingDto;

import java.util.Date;
import java.util.List;

public interface TrainingService {
    TrainingDto createTraining(TrainingDto trainingDto);

    TrainingDto getTrainingById(Integer id);

    List<TrainingDto> getTraineeTrainings(String username, Date fromDate, Date toDate);

    List<TrainingDto> getTrainerTrainings(String username, Date fromDate, Date toDate);
}