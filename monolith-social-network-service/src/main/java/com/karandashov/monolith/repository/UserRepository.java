package com.karandashov.monolith.repository;

import com.karandashov.monolith.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    void save(UserEntity entity);

    Optional<UserEntity> findById(UUID userId);

    void deleteAll();
}
