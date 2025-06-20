package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Сервис для сохранения и получения пользователей приложения.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        log.info("Saved user with username {}", user.getUsername());
        return saved;
    }

    public User update(User user) {
        User updated = save(user);
        log.info("Updated user with id {}", user.getId());
        return updated;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("Deleted user with id {}", id);
    }

    public java.util.List<User> findAll() {
        java.util.List<User> list = userRepository.findAll();
        log.info("Found {} users", list.size());
        return list;
    }

    public Optional<User> findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        log.info("Find user by id {} -> {}", id, user.isPresent());
        return user;
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        log.info("Find user by username {} -> {}", username, user.isPresent());
        return user;
    }

    public Optional<User> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication() == null ? null :
                SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null) {
            log.info("No authenticated user found");
            return Optional.empty();
        }
        log.info("Current authenticated user {}", username);
        return findByUsername(username);
    }
}
