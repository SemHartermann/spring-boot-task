package com.epam.labaratory.springboottask.service.impl;

import com.epam.labaratory.springboottask.service.LoginAttemptService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginAttemptServiceImpl implements LoginAttemptService {

    int MAX_ATTEMPT = 3;
    long BLOCK_TIME_DURATION = 5;

    ConcurrentHashMap<String, UserLoginAttempt> userAttempts = new ConcurrentHashMap<>();

    static class UserLoginAttempt {
        private int attempts = 0;
        private LocalDateTime lockTime = LocalDateTime.now();

        public int getAttempts() {
            return attempts;
        }

        public void setAttempts(int attempts) {
            this.attempts = attempts;
        }

        public LocalDateTime getLockTime() {
            return lockTime;
        }

        public void setLockTime(LocalDateTime lockTime) {
            this.lockTime = lockTime;
        }
    }

    public void loginSucceeded(String key) {
        userAttempts.remove(key);
    }

    public void loginFailed(String key) {
        UserLoginAttempt attempts = userAttempts.get(key);
        if (attempts == null) {
            attempts = new UserLoginAttempt();
            userAttempts.put(key, attempts);
        }
        attempts.setAttempts(attempts.getAttempts() + 1);
        if (attempts.getAttempts() >= MAX_ATTEMPT) {
            attempts.setLockTime(LocalDateTime.now().plusMinutes(BLOCK_TIME_DURATION));
        }
        userAttempts.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        UserLoginAttempt attempts = userAttempts.get(key);
        if (attempts != null && attempts.getAttempts() >= MAX_ATTEMPT) {
            return attempts.getLockTime().isAfter(LocalDateTime.now());
        }
        return false;
    }

    public LocalDateTime getUnlockTime(String key) {
        UserLoginAttempt attempts = userAttempts.get(key);
        if (attempts != null) {
            return attempts.getLockTime();
        }
        return null;
    }
}