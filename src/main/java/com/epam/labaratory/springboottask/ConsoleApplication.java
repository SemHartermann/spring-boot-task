package com.epam.labaratory.springboottask;

import com.epam.labaratory.springboottask.dto.*;
import com.epam.labaratory.springboottask.service.*;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.sql.Date;
import java.util.Scanner;

@SpringBootApplication
public class ConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsoleApplication.class, args);
    }
}

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ConsoleAppRunner implements CommandLineRunner {

    TraineeService traineeService;
    TrainerService trainerService;
    UserService userService;
    AuthService authService;
    TrainingService trainingService;
    TrainingTypeService trainingTypeService;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Gym Management Console Application!");

        while (true) {
            System.out.println("\nEnter a command (type 'help' for list of commands, 'exit' to quit):");
            String command = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(command)) {
                break;
            }

            processCommand(command, scanner);
        }
    }

    private void processCommand(String command, Scanner scanner) {
        switch (command.toLowerCase()) {
            case "help":
                showHelp();
                break;
            case "create_trainee":
                createTrainee(scanner);
                break;
            case "get_trainee":
                getTrainee(scanner);
                break;
            case "update_trainee":
                updateTrainee(scanner);
                break;
            case "create_trainer":
                createTrainer(scanner);
                break;
            case "get_trainer":
                getTrainer(scanner);
                break;
            case "update_trainer":
                updateTrainer(scanner);
                break;
            case "create_training":
                createTraining(scanner);
                break;
            case "get_training":
                getTraining(scanner);
                break;
            case "get_training_type":
                getTrainingType(scanner);
                break;
            case "authenticate":
                authenticate(scanner);
                break;
            default:
                System.out.println("Unknown command. Type 'help' for the list of available commands.");
        }
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("authenticate - Authenticate a user by username and password");
        System.out.println("create_trainee - Create a new trainee");
        System.out.println("get_trainee - Get details of a trainee by username");
        System.out.println("update_trainee - Update details of a trainee");
        System.out.println("create_trainer - Create a new trainer");
        System.out.println("get_trainer - Get details of a trainer by username");
        System.out.println("update_trainer - Update details of a trainer");
        System.out.println("create_training - Create a new training session");
        System.out.println("get_training - Get details of a training session by ID");
        System.out.println("get_training_type - Get details of a training type by name");
        System.out.println("exit - Exit the application");
    }

    private void createTrainee(Scanner scanner) {
        System.out.println("Enter first name:");
        String firstName = scanner.nextLine().trim();

        System.out.println("Enter last name:");
        String lastName = scanner.nextLine().trim();

        System.out.println("Enter address (optional):");
        String address = scanner.nextLine().trim();

        System.out.println("Enter date of birth (yyyy-mm-dd) (optional):");
        String dobInput = scanner.nextLine().trim();
        Date dateOfBirth = null;
        if (!dobInput.isEmpty()) {
            dateOfBirth = Date.valueOf(dobInput);
        }

        UserRegisterDto userDto = new UserRegisterDto(firstName, lastName);
        TraineeRegisterDto traineeDto = new TraineeRegisterDto();
        traineeDto.setUser(userDto);
        traineeDto.setAddress(address);
        traineeDto.setDateOfBirth(dateOfBirth);

        TraineeResponseDto response = traineeService.createTrainee(traineeDto);
        System.out.println("Trainee created with ID: " + response.getId());
    }

    private void getTrainee(Scanner scanner) {
        System.out.println("Enter username of the trainee:");
        String username = scanner.nextLine().trim();

        try {
            TraineeResponseDto trainee = traineeService.getTraineeByUsername(username);
            System.out.println("Trainee details: " + trainee);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateTrainee(Scanner scanner) {
        System.out.println("Enter username of the trainee to update:");
        String username = scanner.nextLine().trim();

        System.out.println("Enter new first name (leave empty to keep current):");
        String firstName = scanner.nextLine().trim();

        System.out.println("Enter new last name (leave empty to keep current):");
        String lastName = scanner.nextLine().trim();

        System.out.println("Enter new address (leave empty to keep current):");
        String address = scanner.nextLine().trim();

        System.out.println("Enter new date of birth (yyyy-mm-dd) (leave empty to keep current):");
        String dobInput = scanner.nextLine().trim();
        Date dateOfBirth = null;
        if (!dobInput.isEmpty()) {
            dateOfBirth = Date.valueOf(dobInput);
        }

        System.out.println("Is active (true/false) (leave empty to keep current):");
        String isActiveInput = scanner.nextLine().trim();
        Boolean isActive = null;
        if (!isActiveInput.isEmpty()) {
            isActive = Boolean.valueOf(isActiveInput);
        }

        UserRequestDto userDto = new UserRequestDto(firstName, lastName, username, null, isActive);
        TraineeRequestDto traineeDto = new TraineeRequestDto(dateOfBirth, address, userDto);

        TraineeResponseDto response = traineeService.updateTraineeProfile(traineeDto);
        System.out.println("Trainee updated: " + response);
    }

    private void createTrainer(Scanner scanner) {
        System.out.println("Enter first name:");
        String firstName = scanner.nextLine().trim();

        System.out.println("Enter last name:");
        String lastName = scanner.nextLine().trim();

        System.out.println("Enter training type:");
        String trainingTypeName = scanner.nextLine().trim();

        UserRegisterDto userDto = new UserRegisterDto(firstName, lastName);
        TrainingTypeRequestDto trainingTypeDto = new TrainingTypeRequestDto(trainingTypeName);
        TrainerRegisterDto trainerDto = new TrainerRegisterDto(trainingTypeDto, userDto);

        TrainerResponseDto response = trainerService.createTrainer(trainerDto);
        System.out.println("Trainer created with ID: " + response.getId());
    }

    private void getTrainer(Scanner scanner) {
        System.out.println("Enter username of the trainer:");
        String username = scanner.nextLine().trim();

        try {
            TrainerResponseDto trainer = trainerService.getTrainerByUsername(username);
            System.out.println("Trainer details: " + trainer);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateTrainer(Scanner scanner) {
        System.out.println("Enter username of the trainer to update:");
        String username = scanner.nextLine().trim();

        System.out.println("Enter new first name (leave empty to keep current):");
        String firstName = scanner.nextLine().trim();

        System.out.println("Enter new last name (leave empty to keep current):");
        String lastName = scanner.nextLine().trim();

        System.out.println("Enter new training type (leave empty to keep current):");
        String trainingTypeName = scanner.nextLine().trim();

        System.out.println("Is active (true/false) (leave empty to keep current):");
        String isActiveInput = scanner.nextLine().trim();
        Boolean isActive = null;
        if (!isActiveInput.isEmpty()) {
            isActive = Boolean.valueOf(isActiveInput);
        }

        UserRequestDto userDto = new UserRequestDto(firstName, lastName, username, null, isActive);
        TrainingTypeRequestDto trainingTypeDto = new TrainingTypeRequestDto(trainingTypeName);
        TrainerRequestDto trainerDto = new TrainerRequestDto(trainingTypeDto, userDto);

        TrainerResponseDto response = trainerService.updateTrainerProfile(trainerDto);
        System.out.println("Trainer updated: " + response);
    }

    private void createTraining(Scanner scanner) {
        System.out.println("Enter trainee username:");
        String traineeUsername = scanner.nextLine().trim();

        System.out.println("Enter trainer username:");
        String trainerUsername = scanner.nextLine().trim();

        System.out.println("Enter training name:");
        String trainingName = scanner.nextLine().trim();

        System.out.println("Enter training type:");
        String trainingTypeName = scanner.nextLine().trim();

        System.out.println("Enter training date (yyyy-mm-dd):");
        String trainingDateInput = scanner.nextLine().trim();
        Date trainingDate = Date.valueOf(trainingDateInput);

        System.out.println("Enter training duration (in minutes):");
        Integer trainingDuration = Integer.valueOf(scanner.nextLine().trim());

        TrainingCreateDto trainingDto = new TrainingCreateDto(traineeUsername, trainerUsername,
                trainingName, new TrainingTypeRequestDto(trainingTypeName), trainingDate, trainingDuration);

        TrainingResponseDto response = trainingService.createTraining(trainingDto);
        System.out.println("Training created with ID: " + response.getId());
    }

    private void getTraining(Scanner scanner) {
        System.out.println("Enter ID of the training:");
        Integer id = Integer.valueOf(scanner.nextLine().trim());

        try {
            TrainingResponseDto training = trainingService.getTrainingById(id);
            System.out.println("Training details: " + training);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getTrainingType(Scanner scanner) {
        System.out.println("Enter name of the training type:");
        String trainingTypeName = scanner.nextLine().trim();

        try {
            TrainingTypeResponseDto trainingType = trainingTypeService.getByName(trainingTypeName);
            System.out.println("Training Type details: " + trainingType);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void authenticate(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine().trim();

        System.out.println("Enter password:");
        String password = scanner.nextLine().trim();

        try {
            UserResponseDto response = authService.authenticate(username, password);
            System.out.println("User authenticated: " + response);
        } catch (UserPrincipalNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}