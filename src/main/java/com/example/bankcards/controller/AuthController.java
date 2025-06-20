package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.security.JwtTokenProvider;
import com.example.bankcards.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * REST-контроллер, предоставляющий точки входа для аутентификации и
 * регистрации пользователей.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            String token = tokenProvider.generateToken((UserDetails) authentication.getPrincipal());
            log.info("User {} authenticated successfully", username);
            return Collections.singletonMap("token", token);
        } catch (org.springframework.security.core.AuthenticationException ex) {
            log.warn("Authentication failed for user {}", username);
            throw ex;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        userService.save(user);
        log.info("User {} successfully registered", dto.getUsername());
        return ResponseEntity.ok().build();
    }
}
