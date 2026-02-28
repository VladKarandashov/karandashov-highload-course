package com.karandashov.monolith.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public record User(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("second_name")
        String lastName,

        @JsonProperty("birthdate")
        LocalDate birthDate,

        @JsonProperty("biography")
        String biography,

        @JsonProperty("city")
        String city
) {
}
