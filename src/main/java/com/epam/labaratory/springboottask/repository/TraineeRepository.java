package com.epam.labaratory.springboottask.repository;

import com.epam.labaratory.springboottask.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
    Optional<Trainee> findByUserUsername (String username);

    Optional<Trainee> findByUserUsernameAndUserPassword(String user_username, String user_password);

    Integer deleteByUserUsername(String username);
}