package com.karandashov.monolith.entity;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private UUID id;

    private String passwordHash;

    private String firstName;

    private String lastName;

    private ZonedDateTime birthDate;

    private String biography;

    private String city;
}
