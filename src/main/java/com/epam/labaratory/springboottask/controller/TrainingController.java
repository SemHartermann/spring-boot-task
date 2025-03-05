package com.epam.labaratory.springboottask.controller;

import com.epam.labaratory.springboottask.dto.TrainingDto;
import com.epam.labaratory.springboottask.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
@Tag(name = "Training Management", description = "Operations pertaining to trainings")
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping
    @Operation(summary = "Create a new training")
    public ResponseEntity<TrainingDto> createTraining(@RequestBody TrainingDto trainingDto) {
        TrainingDto responseDto = trainingService.createTraining(trainingDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get training by ID")
    public ResponseEntity<TrainingDto> getTrainingById(@RequestParam("id") Integer id) {
        TrainingDto responseDto = trainingService.getTrainingById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/trainee")
    @Operation(summary = "Get trainings list for trainee")
    public ResponseEntity<List<TrainingDto>> getTrainingsForTrainee(
            @RequestParam("username") String username,
            @RequestParam(value = "periodFrom", required = false) Date periodFrom,
            @RequestParam(value = "periodTo", required = false) Date periodTo) {
        List<TrainingDto> trainingList = trainingService.getTraineeTrainings(username, periodFrom, periodTo);
        return new ResponseEntity<>(trainingList, HttpStatus.OK);
    }

    @GetMapping("/trainer")
    @Operation(summary = "Get trainings list for trainer")
    public ResponseEntity<List<TrainingDto>> getTrainingsForTrainer(
            @RequestParam("username") String username,
            @RequestParam(value = "periodFrom", required = false) Date periodFrom,
            @RequestParam(value = "periodTo", required = false) Date periodTo) {
        List<TrainingDto> trainingList = trainingService.getTrainerTrainings(username, periodFrom, periodTo);
        return new ResponseEntity<>(trainingList, HttpStatus.OK);
    }
}