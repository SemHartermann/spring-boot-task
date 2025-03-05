package com.epam.labaratory.springboottask.repository;

import com.epam.labaratory.springboottask.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
    boolean existsByTrainingTypeName(String trainingTypeName);
}