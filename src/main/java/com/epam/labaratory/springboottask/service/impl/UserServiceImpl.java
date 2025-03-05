package com.epam.labaratory.springboottask.service.impl;

import com.epam.labaratory.springboottask.dto.ActivationRequestDto;
import com.epam.labaratory.springboottask.dto.UserRegisterDto;
import com.epam.labaratory.springboottask.dto.UserRequestDto;
import com.epam.labaratory.springboottask.dto.UserResponseDto;
import com.epam.labaratory.springboottask.entity.User;
import com.epam.labaratory.springboottask.repository.UserRepository;
import com.epam.labaratory.springboottask.service.UserService;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    ConversionService conversionService;

    @Transactional
    @Override
    public UserResponseDto createUser(UserRegisterDto userRegisterDto) {
        log.trace("Creating user with first name: {} and last name: {}", userRegisterDto.getFirstName(), userRegisterDto.getLastName());

        String username = generateUsername(userRegisterDto);
        String password = generatePassword();

        User user = new User();
        user.setFirstName(userRegisterDto.getFirstName());
        user.setLastName(userRegisterDto.getLastName());
        user.setUsername(username);
        user.setPassword(password);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        log.debug("User created with username: {}", username);

        return conversionService.convert(savedUser, UserResponseDto.class);
    }

    private String generateUsername(UserRegisterDto userRegisterDto) {
        log.debug("Generating username for user profile: {}", userRegisterDto);

        String baseUsername = userRegisterDto.getFirstName() + "." + userRegisterDto.getLastName();
        int serialNumber = 0;
        String newUsername = baseUsername;

        List<User> allUsers = userRepository.findAll();

        String finalNewUsername = newUsername;
        while (allUsers.stream().anyMatch(u -> u.getUsername().equals(finalNewUsername))) {
            serialNumber++;
            newUsername = baseUsername + serialNumber;
        }

        return newUsername;
    }

    private String generatePassword() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        log.trace("Fetching user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return conversionService.convert(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto getUserById(Integer id) {
        log.trace("Fetching user by id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return conversionService.convert(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto authenticate(String username, String password) {
        log.trace("Authenticating user with username: {}", username);

        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);

        if (user.isPresent()) {
            User u = user.get();
            u.setIsActive(true);
            userRepository.save(u);

            log.debug("User authenticated and activated with username: {}", username);

            return conversionService.convert(u, UserResponseDto.class);
        }

        log.warn("Authentication failed for username: {}", username);

        return null;
    }

    @Override
    public void checkIsActive(UserRequestDto userRequestDto) {
        User user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User with username " + userRequestDto.getUsername() + " not found"));

        if (!user.getIsActive()) {
            throw new IllegalStateException("User is not authenticated");
        }
    }

    @Override
    public UserResponseDto updateUserPassword(UserRequestDto userRequestDto, String newPassword) {
        checkIsActive(userRequestDto);

        log.trace("Updating password for user: {}", userRequestDto.getUsername());

        User user = conversionService.convert(userRequestDto, User.class);
        Objects.requireNonNull(user).setPassword(newPassword);
        User updatedUser = userRepository.save(user);

        log.debug("Password updated for user: {}", user.getUsername());

        return conversionService.convert(updatedUser, UserResponseDto.class);
    }

    @Override
    public UserResponseDto activateUser(UserRequestDto userRequestDto) {
        log.trace("Activating user: {}", userRequestDto.getUsername());

        User user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: "
                        + userRequestDto.getUsername()));
        user.setIsActive(true);
        User updatedUser = userRepository.save(user);

        log.debug("User activated: {}", userRequestDto.getUsername());

        return conversionService.convert(updatedUser, UserResponseDto.class);
    }

    @Override
    public UserResponseDto deactivateUser(UserRequestDto userRequestDto) {
        log.trace("Deactivating user: {}", userRequestDto.getUsername());

        User user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setIsActive(false);
        User updatedUser = userRepository.save(user);

        log.debug("User deactivated: {}", userRequestDto.getUsername());

        return conversionService.convert(updatedUser, UserResponseDto.class);
    }

    private void deactivateAllUsers() {
        log.trace("Deactivating all users");

        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setIsActive(false));
        userRepository.saveAll(users);

        log.debug("All users deactivated");
    }

    public void updateUserStatus(ActivationRequestDto activationRequestDto) {
        User user = userRepository.findByUsername(activationRequestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getIsActive() != activationRequestDto.getIsActive()) {
            user.setIsActive(activationRequestDto.getIsActive());
            userRepository.save(user);
        }
    }

    @PreDestroy
    public void onDestroy() {
        deactivateAllUsers();
    }
}