package com.epam.labaratory.springboottask.service.impl;

import com.epam.labaratory.springboottask.dto.*;
import com.epam.labaratory.springboottask.entity.Trainee;
import com.epam.labaratory.springboottask.entity.Trainer;
import com.epam.labaratory.springboottask.entity.Trainer;
import com.epam.labaratory.springboottask.entity.TrainingType;
import com.epam.labaratory.springboottask.repository.TrainerRepository;
import com.epam.labaratory.springboottask.service.TrainerService;
import com.epam.labaratory.springboottask.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrainerServiceImpl implements TrainerService {
    TrainerRepository trainerRepository;
    UserService userService;
    ConversionService conversionService;

    @Transactional
    @Override
    public TrainerResponseDto createTrainer(TrainerRegisterDto trainerRegisterDto) {
        log.trace("Creating trainer with first name: {} and last name: {}",
                trainerRegisterDto.getUser().getFirstName(), trainerRegisterDto.getUser().getLastName());

        UserResponseDto userDto = userService.createUser(trainerRegisterDto.getUser());

        TrainerResponseDto traineeResponseDto = conversionService.convert(trainerRegisterDto, TrainerResponseDto.class);
        traineeResponseDto.setUser(userDto);

        Trainer trainer = conversionService.convert(traineeResponseDto, Trainer.class);

        Trainer savedTrainer = trainerRepository.save(trainer);

        log.debug("Trainer created with user: {}", userDto.getUsername());

        return conversionService.convert(savedTrainer, TrainerResponseDto.class);
    }

    @Override
    public TrainerResponseDto getTrainerById(Integer id) {
        log.trace("Fetching trainer by id: {}", id);

        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));

        log.debug("Trainer was found by id: {}", id);

        return conversionService.convert(trainer, TrainerResponseDto.class);
    }

    @Override
    public TrainerResponseDto getTrainerByUsername(String username) {
        log.trace("Fetching trainer by username: {}", username);

        Trainer trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));

        log.debug("Trainer was found by username: {}", username);

        return conversionService.convert(trainer, TrainerResponseDto.class);
    }

    @Override
    public TrainerResponseDto authenticate(String username, String password) {
        log.trace("Authenticating trainer with username: {}", username);

        userService.authenticate(username, password);

        Trainer trainer = trainerRepository.findByUserUsername(username).get();

        return conversionService.convert(trainer, TrainerResponseDto.class);
    }

    @Transactional
    @Override
    public TrainerResponseDto updateTrainerProfile(TrainerRequestDto trainerRequestDto) {
        log.trace("Updating profile for trainer: {}", trainerRequestDto.getUser().getUsername());

        Trainer currentTrainer = trainerRepository.findByUserUsername(trainerRequestDto.getUser().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));

        if (!trainerRequestDto.getUser().getFirstName().isBlank() &&
                !trainerRequestDto.getUser().getFirstName().equals(currentTrainer.getUser().getFirstName())) {
            currentTrainer.getUser().setFirstName(trainerRequestDto.getUser().getFirstName());
        }

        if (!trainerRequestDto.getUser().getLastName().isBlank() &&
                !trainerRequestDto.getUser().getLastName().equals(currentTrainer.getUser().getLastName())) {
            currentTrainer.getUser().setLastName(trainerRequestDto.getUser().getLastName());
        }

        if (!trainerRequestDto.getUser().getPassword().isBlank() &&
                !trainerRequestDto.getUser().getPassword().equals(currentTrainer.getUser().getPassword())) {
            currentTrainer.getUser().setPassword(trainerRequestDto.getUser().getPassword());
        }

        if (trainerRequestDto.getUser().getIsActive() != null &&
                !trainerRequestDto.getUser().getIsActive().equals(currentTrainer.getUser().getIsActive())) {
            currentTrainer.getUser().setIsActive(trainerRequestDto.getUser().getIsActive());
        }

        if (trainerRequestDto.getSpecialization() != null &&
                !trainerRequestDto.getSpecialization().equals(currentTrainer.getSpecialization())) {
            currentTrainer.setSpecialization(conversionService.convert(trainerRequestDto.getSpecialization(), TrainingType.class));
        }

        Trainer updatedTrainer = trainerRepository.save(currentTrainer);

        log.debug("Profile updated for trainer: {}", trainerRequestDto.getUser().getUsername());

        return conversionService.convert(updatedTrainer, TrainerResponseDto.class);
    }

    @Override
    public TrainerResponseDto updateTrainerPassword(String username, String newPassword) throws UserPrincipalNotFoundException {
        log.trace("Updating password for trainer: {}", username);

        Trainer trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new UserPrincipalNotFoundException("Trainer not found with username: " + username));
        trainer.getUser().setPassword(newPassword);

        Trainer updatedTrainer = trainerRepository.save(trainer);

        log.debug("Password updated for trainer: {}", username);

        return conversionService.convert(updatedTrainer, TrainerResponseDto.class);
    }
}