package com.currencyconverter.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import static com.currencyconverter.demo.exceptions.BadParameterException.ERROR_CODE;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadParameterException.class)
    public ModelAndView handleBadParameterException(HttpServletRequest request, BadParameterException e) {
        String header = request.getHeader("Accept");
        if (header != null && header.equals("*/*")) {
            return handleBadParameterExceptionRest(e);
        }
        return handleBadParameterExceptionMVC(e);
    }

    private ModelAndView handleBadParameterExceptionMVC(BadParameterException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorCode", ERROR_CODE);
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }

    private ModelAndView handleBadParameterExceptionRest(BadParameterException e) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("error", e.getMessage());
        return modelAndView;
    }
}