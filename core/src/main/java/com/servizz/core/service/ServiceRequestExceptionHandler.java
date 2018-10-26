package com.servizz.core.service;


import com.servizz.core.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class ServiceRequestExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(ServiceRequestExceptionHandler.class);

    @ExceptionHandler(ServiceRequestController.ServiceRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleServiceRequestNotFoundException(ServiceRequestController.ServiceRequestNotFoundException ex) {
        logger.error(Optional.ofNullable(ex.getMessage()).orElse("Service not found!"));
        return ErrorResponse.get(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceRequestController.BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequestException(ServiceRequestController.ServiceRequestNotFoundException ex) {
        logger.error(Optional.ofNullable(ex.getMessage()).orElse("Bad request!"));
        return ErrorResponse.get(ex, HttpStatus.NOT_FOUND);
    }

}
