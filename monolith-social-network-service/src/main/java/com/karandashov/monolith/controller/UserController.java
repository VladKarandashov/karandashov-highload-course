package com.karandashov.monolith.controller;

import com.karandashov.monolith.dto.User;
import com.karandashov.monolith.dto.request.RegisterRequest;
import com.karandashov.monolith.dto.response.RegisterResponse;
import com.karandashov.monolith.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request: {}", request);
        return userService.register(request);
    }

    @GetMapping("/get/{id}")
    public User getById(@PathVariable UUID id) {
        log.info("Get user by id: {}", id);
        return userService.getUserById(id);
    }

    @GetMapping("/search")
    public List<User> search(@RequestParam("first_name") String firstName,
                             @RequestParam("last_name") String lastName) {
        log.info("Search users: {} {}", firstName, lastName);
        return userService.searchUsers(firstName, lastName);
    }
}
