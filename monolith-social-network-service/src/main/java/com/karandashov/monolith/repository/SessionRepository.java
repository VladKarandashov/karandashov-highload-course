package com.karandashov.monolith.repository;

import com.karandashov.monolith.dto.User;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {

    void save(UUID token, User user);

    Optional<User> get(UUID token);

    void deleteAll();
}
