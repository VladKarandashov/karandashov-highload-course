package com.karandashov.monolith.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record LoginRequest(

        @NotNull
        @JsonProperty("id")
        UUID userId,

        @NotBlank
        @Size(min = 5, max = 100)
        String password
) {
}