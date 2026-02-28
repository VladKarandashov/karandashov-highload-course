package com.karandashov.monolith.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record LoginResponse(
        @JsonProperty("token") UUID token
) {
}
