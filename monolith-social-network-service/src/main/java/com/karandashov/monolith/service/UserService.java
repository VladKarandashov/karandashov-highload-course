package com.karandashov.monolith.service;

import com.karandashov.monolith.dto.User;
import com.karandashov.monolith.dto.request.LoginRequest;
import com.karandashov.monolith.dto.request.RegisterRequest;
import com.karandashov.monolith.dto.response.LoginResponse;
import com.karandashov.monolith.dto.response.RegisterResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    User getUserById(UUID id);

    List<User> searchUsers(String firstName, String lastName);

    User checkAuth(UUID token);
}
