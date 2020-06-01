package com.currencyconverter.demo.controllers.restcontrollers;

import com.currencyconverter.demo.exceptions.ExceptionResponse;
import com.currencyconverter.demo.exceptions.BadParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadParameterException.class) // 400
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadParameterException(BadParameterException e) {
        return new ExceptionResponse(e.getMessage());
    }
}
