package com.karandashov.monolith.repository.impl;

import com.karandashov.monolith.dto.User;
import com.karandashov.monolith.repository.SessionRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySessionRepository implements SessionRepository {

    private final Map<UUID, User> sessions = new ConcurrentHashMap<>();

    @Override
    public void save(UUID token, User user) {
        sessions.put(token, user);
    }

    @Override
    public Optional<User> get(UUID token) {
        return Optional.ofNullable(sessions.get(token));
    }

    @Override
    public void deleteAll() {
        sessions.clear();
    }
}
