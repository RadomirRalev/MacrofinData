package com.currencyconverter.demo.exceptions;

import com.currencyconverter.demo.exceptions.client.BadParameterException;
import com.currencyconverter.demo.exceptions.client.ResourceNotFoundException;
import com.currencyconverter.demo.exceptions.server.BadGatewayException;
import com.currencyconverter.demo.exceptions.server.InternalServerErrorECB;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(BadParameterException.class)
    public ModelAndView handleBadParameterException(BadParameterException e) {
        return handleCustomExceptionRest(e.getMessage(), e.getStatus(), e.getTimestamp(), e.getErrorCode());

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleWrongURLException(ResourceNotFoundException e) {
        return handleCustomExceptionRest(e.getMessage(), e.getStatus(), e.getTimestamp(), e.getErrorCode());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(InternalServerErrorECB.class)
    public ModelAndView handleInternalServerErrorExceptionECB(BadGatewayException e) {
        return handleCustomExceptionRest(e.getMessage(), e.getStatus(), e.getTimestamp(), e.getErrorCode());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_GATEWAY)  // 502
    @ExceptionHandler(BadGatewayException.class)
    public ModelAndView handleNoResponseFromServerExceptionException(BadGatewayException e) {
        return handleCustomExceptionRest(e.getMessage(), e.getStatus(), e.getTimestamp(), e.getErrorCode());
    }

    private ModelAndView handleCustomExceptionRest(String message, HttpStatus status, LocalDateTime timestamp, int errorCode) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("status", status);
        modelAndView.addObject("timestamp", timestamp.toString());
        modelAndView.addObject("message", message);
        modelAndView.addObject("error code", errorCode);
        return modelAndView;
    }
}