package com.karandashov.monolith.exception;

import lombok.Getter;

public class BaseException extends RuntimeException {

    @Getter
    private final Error error;

    public BaseException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

    public BaseException(Error error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
    }
}
