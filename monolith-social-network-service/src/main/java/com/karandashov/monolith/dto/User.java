package com.karandashov.monolith.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record User(
        UUID id,

        String passwordHash,

        String firstName,

        String lastName,

        ZonedDateTime birthDate,

        String biography,

        String city
) {
}
