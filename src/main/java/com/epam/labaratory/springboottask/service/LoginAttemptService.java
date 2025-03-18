package com.epam.labaratory.springboottask.service;

import java.time.LocalDateTime;

public interface LoginAttemptService {
    void loginSucceeded(String key);

    void loginFailed(String key);

    boolean isBlocked(String key);

    LocalDateTime getUnlockTime(String key);
}
