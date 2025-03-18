package com.epam.labaratory.springboottask.controller;

import com.epam.labaratory.springboottask.dto.*;
import com.epam.labaratory.springboottask.service.AuthService;
import com.epam.labaratory.springboottask.service.TraineeService;
import com.epam.labaratory.springboottask.service.TrainingService;
import com.epam.labaratory.springboottask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/trainees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Trainee Management", description = "Operations pertaining to trainees")
public class TraineeController {

    UserService userService;
    TraineeService traineeService;
    TrainingService trainingService;
    AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new trainee")
    public ResponseEntity<RegisterResponseDto<TraineeResponseDto>> registerTrainee(@Validated @RequestBody TraineeRegisterDto traineeRegisterDto) {
        RegisterResponseDto<TraineeResponseDto> responseDto = traineeService.createTrainee(traineeRegisterDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login as a trainee")
    public ResponseEntity<AuthenticationResponseDto> loginTrainee(@RequestBody AuthenticationRequestDto loginRequest) throws UserPrincipalNotFoundException {
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change trainee login password",
            security = {@SecurityRequirement(name = "Authorization")})
    public ResponseEntity<Void> changePassword(
            @RequestParam("username") String username,
            @RequestParam("newPassword") String newPassword) throws UserPrincipalNotFoundException {
        traineeService.updateTraineePassword(username, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get trainee profile",
            security = {@SecurityRequirement(name = "Authorization")})
    public ResponseEntity<TraineeResponseDto> getProfile(@RequestParam("username") String username) {
        TraineeResponseDto responseDto = traineeService.getTraineeByUsername(username);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update trainee profile",
            security = {@SecurityRequirement(name = "Authorization")})
    public ResponseEntity<TraineeResponseDto> updateProfile(@Validated @RequestBody TraineeRequestDto traineeRequestDto) {
        TraineeResponseDto responseDto = traineeService.updateTraineeProfile(traineeRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/profile")
    @Operation(summary = "Delete trainee profile",
            security = {@SecurityRequirement(name = "Authorization")})
    public ResponseEntity<Void> deleteProfile(@RequestParam("username") String username) {
        traineeService.deleteTraineeProfileByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/trainings")
    @Operation(summary = "Get trainee trainings list",
            security = {@SecurityRequirement(name = "Authorization")})
    public ResponseEntity<List<TrainingResponseDto>> getTraineeTrainingsList(
            @RequestParam("username") String username,
            @RequestParam(value = "periodFrom", required = false) Date periodFrom,
            @RequestParam(value = "periodTo", required = false) Date periodTo) {
        List<TrainingResponseDto> trainingList = trainingService.getTraineeTrainings(username, periodFrom, periodTo);
        return new ResponseEntity<>(trainingList, HttpStatus.OK);
    }

    @PatchMapping("/activate")
    @Operation(summary = "Activate or Deactivate Trainee",
            security = {@SecurityRequirement(name = "Authorization")})
    public ResponseEntity<Void> activate(@RequestBody ActivationRequestDto activationRequestDto) {
        userService.updateUserStatus(activationRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout Trainee",
            security = {@SecurityRequirement(name = "Authorization")})
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return "You have been logged out.";
    }
}