package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * Контроллер управления пользователями (обновление/удаление).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public java.util.List<UserDto> all() {
        java.util.List<UserDto> list = userService.findAll().stream()
                .map(UserDto::fromEntity)
                .toList();
        log.info("Retrieved {} users", list.size());
        return list;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        userService.save(user);
        log.info("Created user {}", dto.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        User user = userService.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        userService.update(user);
        log.info("Updated user with id {}", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        log.info("Deleted user with id {}", id);
        return ResponseEntity.noContent().build();
    }
}
