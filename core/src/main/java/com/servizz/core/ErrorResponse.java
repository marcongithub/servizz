package com.servizz.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class ErrorResponse {

    public static final String DEFAULT_ERROR_MSG = "An error occured.";
    private final String message;


    private ErrorResponse(String message) {
        this.message = message;
    }

    public static ResponseEntity<ErrorResponse> get(@NotNull Exception ex, @NotNull HttpStatus httpStatus) {
        String message = Optional.ofNullable(ex.getMessage()).orElse(DEFAULT_ERROR_MSG);
        return new ResponseEntity<>(new ErrorResponse(message), httpStatus);
    }

    public String getMessage() {
        return message;
    }
}
