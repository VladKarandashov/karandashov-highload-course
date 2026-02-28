package com.karandashov.monolith.controller;

import com.karandashov.monolith.dto.request.LoginRequest;
import com.karandashov.monolith.dto.response.LoginResponse;
import com.karandashov.monolith.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
