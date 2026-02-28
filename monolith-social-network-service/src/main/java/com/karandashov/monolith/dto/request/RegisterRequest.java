package com.karandashov.monolith.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterRequest(

        @NotBlank
        @Size(min = 5, max = 100)
        @JsonProperty("password")
        String password,

        @NotBlank
        @Size(min = 3, max = 30)
        @JsonProperty("first_name")
        String firstName,

        @NotBlank
        @Size(min = 3, max = 30)
        @JsonProperty("second_name")
        String lastName,

        @NotNull
        @PastOrPresent
        @JsonProperty("birthdate")
        LocalDate birthdate,

        @Size(max = 4096)
        @JsonProperty("biography")
        String biography,

        @Size(max = 100)
        @JsonProperty("city")
        String city
) {
}
