package com.epam.labaratory.springboottask.service.impl;

import com.epam.labaratory.springboottask.dto.TrainingDto;
import com.epam.labaratory.springboottask.entity.Training;
import com.epam.labaratory.springboottask.repository.TrainingRepository;
import com.epam.labaratory.springboottask.service.TrainingService;
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
    ConversionService conversionService;

    @Transactional
    @Override
    public TrainingDto createTraining(TrainingDto trainingDto) {
        log.trace("Adding training {}", trainingDto);

        Training training = conversionService.convert(trainingDto, Training.class);
        training = trainingRepository.save(Objects.requireNonNull(training));

        log.debug("Training added {}", training);
        return conversionService.convert(training, TrainingDto.class);
    }

    @Override
    public TrainingDto getTrainingById(Integer id) {
        log.trace("Fetching training by id: {}", id);

        Training training = trainingRepository.findById(id).get();

        log.debug("Training was found by id: {}", id);

        return conversionService.convert(training, TrainingDto.class);
    }

    @Override
    public List<TrainingDto> getTraineeTrainings(String username, Date fromDate, Date toDate) {
        log.trace("Fetching trainings for trainee: {} from date: {} to date: {}", username, fromDate, toDate);

        return trainingRepository.findAllByTraineeUserUsernameAndTrainingDateBetween(username, fromDate, toDate)
                .stream()
                .map(training -> conversionService.convert(training, TrainingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingDto> getTrainerTrainings(String username, Date fromDate, Date toDate) {
        log.trace("Fetching trainings for trainer: {} from date: {} to date: {}", username, fromDate, toDate);

        return trainingRepository.findAllByTrainerUserUsernameAndTrainingDateBetween(username, fromDate, toDate)
                .stream()
                .map(training -> conversionService.convert(training, TrainingDto.class))
                .collect(Collectors.toList());
    }
}