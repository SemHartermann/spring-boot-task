package com.epam.labaratory.springboottask.service.impl;

import com.epam.labaratory.springboottask.dto.*;
import com.epam.labaratory.springboottask.entity.Trainee;
import com.epam.labaratory.springboottask.entity.Trainer;
import com.epam.labaratory.springboottask.entity.Training;
import com.epam.labaratory.springboottask.entity.TrainingType;
import com.epam.labaratory.springboottask.repository.TrainingRepository;
import com.epam.labaratory.springboottask.service.TraineeService;
import com.epam.labaratory.springboottask.service.TrainerService;
import com.epam.labaratory.springboottask.service.TrainingService;
import com.epam.labaratory.springboottask.service.TrainingTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrainingServiceImpl implements TrainingService {
    TrainingRepository trainingRepository;
    TraineeService traineeService;
    TrainerService trainerService;
    TrainingTypeService trainingTypeService;
    ConversionService conversionService;

    @Transactional
    @Override
    public TrainingResponseDto createTraining(TrainingCreateDto trainingDto) {
        log.trace("Adding training {}", trainingDto);

        TraineeResponseDto traineeResponseDto = traineeService.getTraineeByUsername(trainingDto.getTraineeUsername());
        TrainerResponseDto trainerResponseDto = trainerService.getTrainerByUsername(trainingDto.getTrainerUsername());
        TrainingTypeResponseDto trainingTypeResponseDto = trainingTypeService.getByName(trainingDto.getTrainingType().getTrainingTypeName());

        Training training = conversionService.convert(trainingDto, Training.class);
        training.setTrainer(conversionService.convert(trainerResponseDto, Trainer.class));
        training.setTrainee(conversionService.convert(traineeResponseDto, Trainee.class));
        training.setTrainingType(conversionService.convert(trainingTypeResponseDto, TrainingType.class));

        training = trainingRepository.save(Objects.requireNonNull(training));

        log.debug("Training added {}", training);
        return conversionService.convert(training, TrainingResponseDto.class);
    }

    @Override
    public TrainingResponseDto getTrainingById(Integer id) {
        log.trace("Fetching training by id: {}", id);

        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Training not found"));

        log.debug("Training was found by id: {}", id);

        return conversionService.convert(training, TrainingResponseDto.class);
    }

    @Override
    public List<TrainingResponseDto> getTraineeTrainings(String username, Date fromDate, Date toDate) {
        log.trace("Fetching trainings for trainee: {} from date: {} to date: {}", username, fromDate, toDate);

        return trainingRepository.findAllByTraineeUserUsernameAndTrainingDateBetween(username, fromDate, toDate)
                .stream()
                .map(training -> conversionService.convert(training, TrainingResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingResponseDto> getTrainerTrainings(String username, Date fromDate, Date toDate) {
        log.trace("Fetching trainings for trainer: {} from date: {} to date: {}", username, fromDate, toDate);

        return trainingRepository.findAllByTrainerUserUsernameAndTrainingDateBetween(username, fromDate, toDate)
                .stream()
                .map(training -> conversionService.convert(training, TrainingResponseDto.class))
                .collect(Collectors.toList());
    }
}