package com.karandashov.monolith.controller;

import com.karandashov.monolith.dto.User;
import com.karandashov.monolith.dto.request.LoginRequest;
import com.karandashov.monolith.dto.response.LoginResponse;
import com.karandashov.monolith.exception.BaseException;
import com.karandashov.monolith.exception.Error;
import com.karandashov.monolith.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request: {}", request);
        var response = userService.login(request);
        log.info("Login response: {}", response);
        return response;
    }

    @PostMapping("/me")
    public User me(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        log.info("Request to /me with Authorization: {}", authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BaseException(Error.UNAUTHORIZED);
        }
        String tokenStr = authHeader.substring(7);
        try {
            UUID token = UUID.fromString(tokenStr);
            return userService.checkAuth(token);
        } catch (IllegalArgumentException e) {
            throw new BaseException(Error.UNAUTHORIZED);
        }
    }
}
