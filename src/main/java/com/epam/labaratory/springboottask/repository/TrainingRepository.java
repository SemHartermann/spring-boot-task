package com.epam.labaratory.springboottask.repository;

import com.epam.labaratory.springboottask.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {
    List<Training> findAllByTraineeUserUsernameAndTrainingDateBetween(String username, Date fromDate, Date toDate);

    List<Training> findAllByTrainerUserUsernameAndTrainingDateBetween(String username, Date fromDate, Date toDate);
}