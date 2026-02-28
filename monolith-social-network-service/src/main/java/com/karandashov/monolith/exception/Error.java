package com.karandashov.monolith.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Error {

    BAD_REQUEST_USER_NOT_FOUND(HttpStatus.NOT_FOUND, 4001, "User not found"),
    BAD_REQUEST_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, 4002, "Incorrect password"),

    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "Unexpected error"),

    SQL_FAILED_SAVE_USER(HttpStatus.INTERNAL_SERVER_ERROR, 5001, "Failed to save user"),
    SQL_FAILED_DELETE_USER(HttpStatus.INTERNAL_SERVER_ERROR, 5002, "Failed to delete user"),
    SQL_FAILED_GET_USER_BY_ID(HttpStatus.INTERNAL_SERVER_ERROR, 5003, "Failed to get user by id");

    private final HttpStatus httpStatusCode;

    private final int errorCode;

    private final String message;

    Error(HttpStatus httpStatusCode, int errorCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.message = message;
    }
}
