package com.karandashov.monolith.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(
        @JsonProperty("code") Integer code,
        @JsonProperty("message") String message,
        @JsonProperty("request_id") String requestId
) {
}
