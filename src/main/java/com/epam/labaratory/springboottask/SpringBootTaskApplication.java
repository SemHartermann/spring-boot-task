package com.epam.labaratory.springboottask;

import com.epam.labaratory.springboottask.entity.TrainingType;
import com.epam.labaratory.springboottask.repository.TrainingTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SpringBootTaskApplication {

    private static final List<String> DEFAULT_TRAINING_TYPES = Arrays.asList(
            "CARDIO_TRAINING",
            "STRENGTH_TRAINING",
            "FLEXIBILITY_TRAINING",
            "BALANCE_TRAINING",
            "DIET_CONSULTATION",
            "WEIGHT_LOSS_PILATES_TRAINING",
            "YOGA_TRAINING",
            "DANCE_TRAINING"
    );

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootTaskApplication.class, args);
        initializeTrainingTypes(context);
    }

    private static void initializeTrainingTypes(ConfigurableApplicationContext context) {
        TrainingTypeRepository trainingTypeRepository = context.getBean(TrainingTypeRepository.class);

        for (String trainingTypeName : DEFAULT_TRAINING_TYPES) {
            if (!trainingTypeRepository.existsByTrainingTypeName(trainingTypeName)) {
                trainingTypeRepository.save(new TrainingType(trainingTypeName));
            }
        }
    }
}
