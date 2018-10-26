package com.servizz.core;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class ErrorResponseTest {

    //TODO run with parameters
    @Test
    public void get() {
        ResponseEntity<ErrorResponse> responseEntity = ErrorResponse.get(new Exception("ex msg"), HttpStatus.NOT_FOUND);
        assertEquals("Error message didnt match!", "ex msg", responseEntity.getBody().getMessage());
        assertEquals("http status didnt match!", HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getWithDefaultErrorMsg() {
        ResponseEntity<ErrorResponse> responseEntity = ErrorResponse.get(new Exception(), HttpStatus.NOT_FOUND);
        assertEquals("Error message didnt match!", ErrorResponse.DEFAULT_ERROR_MSG, responseEntity.getBody().getMessage());
        assertEquals("http status didnt match!", HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
