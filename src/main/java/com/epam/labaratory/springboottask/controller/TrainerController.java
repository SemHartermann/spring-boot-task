package com.epam.labaratory.springboottask.controller;

import com.epam.labaratory.springboottask.dto.*;
import com.epam.labaratory.springboottask.service.AuthService;
import com.epam.labaratory.springboottask.service.TraineeService;
import com.epam.labaratory.springboottask.service.TrainerService;
import com.epam.labaratory.springboottask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Trainer Management", description = "Operations pertaining to trainers")
public class TrainerController {

    TrainerService trainerService;
    TraineeService traineeService;
    UserService userService;
    AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new trainer")
    public ResponseEntity<TrainerResponseDto> registerTrainer(@Validated @RequestBody TrainerRegisterDto trainerRegisterDto) {
        TrainerResponseDto responseDto = trainerService.createTrainer(trainerRegisterDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login as a trainer")
    public ResponseEntity<String> loginTrainer(@RequestBody LoginRequestDto loginRequest) throws UserPrincipalNotFoundException {
        UserResponseDto user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        return new ResponseEntity<>("Username: " + user.getUsername(), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change trainer login password",
            security = {@SecurityRequirement(name = "Authorization")})
    public ResponseEntity<Void> changePassword(
            @RequestParam("username") String username,
            @RequestParam("newPassword") String newPassword) throws UserPrincipalNotFoundException {
        trainerService.updateTrainerPassword(username, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get trainer profile",
            security = {@SecurityRequirement(name = "Authorization")})
    public ResponseEntity<TrainerResponseDto> getProfile(@RequestParam("username") String username) {
        TrainerResponseDto responseDto = trainerService.getTrainerByUsername(username);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update trainer profile",
            security = {@SecurityRequirement(name = "Authorization")})
    public ResponseEntity<TrainerResponseDto> updateProfile(@Validated @RequestBody TrainerRequestDto trainerRequestDto) {
        TrainerResponseDto responseDto = trainerService.updateTrainerProfile(trainerRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/unassigned")
    @Operation(summary = "Get not assigned on trainee active trainers",
            security = {@SecurityRequirement(name = "Authorization")})
    public ResponseEntity<List<TrainerResponseDto>> getUnassignedTrainers(
            @RequestParam("username") String username) {
        List<TrainerResponseDto> unassignedTrainers = traineeService.getUnassignedTrainers(username);
        return new ResponseEntity<>(unassignedTrainers, HttpStatus.OK);
    }

    @PatchMapping("/activate")
    @Operation(summary = "Activate or Deactivate Trainer",
            security = {@SecurityRequirement(name = "Authorization")})
    public ResponseEntity<Void> activate(@RequestBody ActivationRequestDto activationRequestDto) {
        userService.updateUserStatus(activationRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}