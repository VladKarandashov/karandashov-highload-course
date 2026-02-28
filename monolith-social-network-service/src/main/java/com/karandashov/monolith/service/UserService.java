package com.karandashov.monolith.service;

import com.karandashov.monolith.dto.request.LoginRequest;
import com.karandashov.monolith.dto.request.RegisterRequest;
import com.karandashov.monolith.dto.response.LoginResponse;
import com.karandashov.monolith.dto.response.RegisterResponse;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
