package com.karandashov.monolith.controller;

import com.karandashov.monolith.dto.request.RegisterRequest;
import com.karandashov.monolith.dto.response.RegisterResponse;
import com.karandashov.monolith.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request: {}", request);
        var response = userService.register(request);
        log.info("Register response: {}", response);
        return response;
    }
}
