package com.epam.labaratory.springboottask.service.impl;

import com.epam.labaratory.springboottask.dto.*;
import com.epam.labaratory.springboottask.entity.Trainee;
import com.epam.labaratory.springboottask.entity.Trainer;
import com.epam.labaratory.springboottask.repository.TraineeRepository;
import com.epam.labaratory.springboottask.repository.TrainerRepository;
import com.epam.labaratory.springboottask.repository.TrainingRepository;
import com.epam.labaratory.springboottask.service.AuthService;
import com.epam.labaratory.springboottask.service.TraineeService;
import com.epam.labaratory.springboottask.service.UserService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TraineeServiceImpl implements TraineeService {

    TraineeRepository traineeRepository;
    TrainingRepository trainingRepository;
    TrainerRepository trainerRepository;
    UserService userService;
    ConversionService conversionService;
    AuthService authService;

    public TraineeServiceImpl(TraineeRepository traineeRepository, TrainingRepository trainingRepository, TrainerRepository trainerRepository, UserService userService, ConversionService conversionService,
                              MeterRegistry meterRegistry, AuthService authService) {
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
        this.trainerRepository = trainerRepository;
        this.userService = userService;
        this.conversionService = conversionService;
        this.authService = authService;

        Gauge.builder("api_trainee_count", getCompanyCount())
                .description("Trainees Count")
                .register(meterRegistry);
    }

    private Supplier<Number> getCompanyCount() {
        return traineeRepository::count;
    }

    @Transactional
    @Override
    public RegisterResponseDto<TraineeResponseDto> createTrainee(TraineeRegisterDto traineeRegisterDto) {
        log.trace("Creating trainee with first name: {} and last name: {}",
                traineeRegisterDto.getUser().getFirstName(), traineeRegisterDto.getUser().getLastName());

        UserResponseDto userDto = userService.createUser(traineeRegisterDto.getUser());

        TraineeResponseDto traineeResponseDto = conversionService.convert(traineeRegisterDto, TraineeResponseDto.class);
        traineeResponseDto.setUser(userDto);

        Trainee trainee = conversionService.convert(traineeResponseDto, Trainee.class);

        Trainee savedTrainee = traineeRepository.save(trainee);

        log.debug("Trainee created with user: {}", userDto.getUsername());

        traineeResponseDto = conversionService.convert(savedTrainee, TraineeResponseDto.class);

        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto();
        authenticationRequestDto.setUsername(traineeResponseDto.getUser().getUsername());
        authenticationRequestDto.setPassword(traineeResponseDto.getUser().getPassword());

        RegisterResponseDto<TraineeResponseDto> registerResponseDto = new RegisterResponseDto<>();
        registerResponseDto.setAccessToken(authService.login(authenticationRequestDto).getAccessToken());
        registerResponseDto.setInfo(traineeResponseDto);

        return registerResponseDto;
    }

    @Override
    public TraineeResponseDto authenticate(String username, String password) {
        log.trace("Authenticating trainee with username: {}", username);

        userService.authenticate(username, password);

        Trainee trainee = traineeRepository.findByUserUsername(username).get();

        return conversionService.convert(trainee, TraineeResponseDto.class);
    }

    @Override
    public TraineeResponseDto getTraineeByUsername(String username) {
        log.trace("Fetching trainee by username: {}", username);

        Trainee trainee = traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));

        log.debug("Trainee was found by username: {}", username);

        return conversionService.convert(trainee, TraineeResponseDto.class);
    }

    @Override
    public TraineeResponseDto getTraineeById(Integer id) {
        log.trace("Fetching trainee by id: {}", id);

        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));

        log.debug("Trainee was found by id: {}", id);

        return conversionService.convert(trainee, TraineeResponseDto.class);
    }

    @Override
    public TraineeResponseDto updateTraineeProfile(TraineeRequestDto traineeRequestDto) {
        log.trace("Updating profile for trainee: {}", traineeRequestDto.getUser().getUsername());

        TraineeResponseDto currentTrainee = getTraineeByUsername(traineeRequestDto.getUser().getUsername());

        if (!traineeRequestDto.getUser().getFirstName().isBlank() &&
                !traineeRequestDto.getUser().getFirstName().equals(currentTrainee.getUser().getFirstName())) {
            currentTrainee.getUser().setFirstName(traineeRequestDto.getUser().getFirstName());
        }

        if (!traineeRequestDto.getUser().getLastName().isBlank() &&
                !traineeRequestDto.getUser().getLastName().equals(currentTrainee.getUser().getLastName())) {
            currentTrainee.getUser().setLastName(traineeRequestDto.getUser().getLastName());
        }

        if (!traineeRequestDto.getUser().getPassword().isBlank() &&
                !traineeRequestDto.getUser().getPassword().equals(currentTrainee.getUser().getPassword())) {
            currentTrainee.getUser().setPassword(traineeRequestDto.getUser().getPassword());
        }

        if (traineeRequestDto.getUser().getIsActive() != null &&
                !traineeRequestDto.getUser().getIsActive().equals(currentTrainee.getUser().getIsActive())) {
            currentTrainee.getUser().setIsActive(traineeRequestDto.getUser().getIsActive());
        }

        if (!traineeRequestDto.getAddress().isBlank() &&
                !traineeRequestDto.getAddress().equals(currentTrainee.getAddress())) {
            currentTrainee.setAddress(traineeRequestDto.getAddress());
        }

        if (traineeRequestDto.getDateOfBirth() != null &&
                !traineeRequestDto.getDateOfBirth().toString().isBlank() &&
                !traineeRequestDto.getDateOfBirth().equals(currentTrainee.getDateOfBirth())) {
            currentTrainee.setDateOfBirth(traineeRequestDto.getDateOfBirth());
        }

        Trainee updatedTrainee = conversionService.convert(currentTrainee, Trainee.class);
        updatedTrainee = traineeRepository.save(updatedTrainee);

        log.debug("Profile updated for trainee: {}", traineeRequestDto.getUser().getUsername());

        return conversionService.convert(updatedTrainee, TraineeResponseDto.class);
    }

    @Override
    public TraineeResponseDto updateTraineePassword(String username, String newPassword) throws UserPrincipalNotFoundException {
        log.trace("Updating password for trainee: {}", username);

        Trainee trainee = traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> new UserPrincipalNotFoundException("Trainee not found with username: " + username));
        trainee.getUser().setPassword(newPassword);

        Trainee updatedTrainee = traineeRepository.save(trainee);

        log.debug("Password updated for trainee: {}", username);

        return conversionService.convert(updatedTrainee, TraineeResponseDto.class);
    }

    @Override
    public void deleteTraineeProfileByUsername(String username) {
        log.trace("Deleting trainee profile by username: {}", username);

        traineeRepository.deleteByUserUsername(username);

        log.debug("Trainee profile deleted: {}", username);
    }

    @Override
    public List<TrainingResponseDto> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType) {
        log.trace("Fetching trainings for trainee: {} from date: {} to date: {}", username, fromDate, toDate);

        return trainingRepository.findAllByTrainerUserUsernameAndTrainingDateBetween(username, fromDate, toDate)
                .stream()
                .map(training -> conversionService.convert(training, TrainingResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override

    public List<TrainerResponseDto> getUnassignedTrainers(String username) {
        log.trace("Fetching unassigned trainers for trainee: {}", username);

        return trainerRepository.findUnassignedTrainersByTraineeUsername(username).stream()
                .map(trainer -> conversionService.convert(trainer, TrainerResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TraineeResponseDto updateTraineeTrainersList(String traineeUsername, List<String> trainerUsernames) {
        log.trace("Updating trainer list for trainee username: {}", traineeUsername);

        Trainee trainee = traineeRepository.findByUserUsername(traineeUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trainee username"));

        trainee.getTrainers().clear();

        trainerUsernames.forEach(trainerUsername -> {
            Trainer trainer = trainerRepository.findByUserUsername(trainerUsername)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid trainer username"));
            trainee.getTrainers().add(trainer);
        });

        Trainee updatedTrainee = traineeRepository.save(trainee);

        log.debug("Trainer list updated for trainee username: {}", traineeUsername);

        return conversionService.convert(updatedTrainee, TraineeResponseDto.class);
    }
}