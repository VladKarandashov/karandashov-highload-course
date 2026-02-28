package com.karandashov.monolith.service.impl;

import com.karandashov.monolith.dto.request.LoginRequest;
import com.karandashov.monolith.dto.request.RegisterRequest;
import com.karandashov.monolith.dto.response.LoginResponse;
import com.karandashov.monolith.dto.response.RegisterResponse;
import com.karandashov.monolith.exception.BaseException;
import com.karandashov.monolith.exception.Error;
import com.karandashov.monolith.mapper.UserMapper;
import com.karandashov.monolith.repository.SessionRepository;
import com.karandashov.monolith.repository.UserRepository;
import com.karandashov.monolith.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        var entity = userMapper.toEntity(request);
        entity.setId(UUID.randomUUID());
        entity.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(entity);
        return new RegisterResponse(entity.getId());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        var userEntity = userRepository.findById(request.userId())
                .orElseThrow(() -> new BaseException(Error.BAD_REQUEST_USER_NOT_FOUND));
        var expectedPasswordHash = userEntity.getPasswordHash();
        var requestPasswordHash = passwordEncoder.encode(request.password());
        if (!expectedPasswordHash.equals(requestPasswordHash)) {
            throw new BaseException(Error.BAD_REQUEST_INVALID_PASSWORD);
        }
        var user = userMapper.toDto(userEntity);
        var token = UUID.randomUUID();
        sessionRepository.save(token, user);
        return new LoginResponse(token);
    }
}
