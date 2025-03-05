package com.epam.labaratory.springboottask.repository;

import com.epam.labaratory.springboottask.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    Optional<Trainer> findByUserUsername (String username);

    Optional<Trainer> findByUserUsernameAndUserPassword(String user_username, String user_password);

    Integer deleteByUserUsername(String username);

    @Query("SELECT tr FROM Trainer tr WHERE tr.id NOT IN (SELECT t.id FROM Trainer t JOIN t.trainees trn WHERE trn.user.username = :traineeUsername)")
    List<Trainer> findUnassignedTrainersByTraineeUsername(@Param("traineeUsername") String traineeUsername);
}