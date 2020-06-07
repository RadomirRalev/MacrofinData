package com.currencyconverter.demo.exceptions;

import com.currencyconverter.demo.exceptions.implementations.BadParameterException;
import com.currencyconverter.demo.exceptions.implementations.WrongURLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static com.currencyconverter.demo.constants.ControllerConstants.*;


@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(BadParameterException.class)
    public ModelAndView handleBadParameterException(HttpServletRequest request, BadParameterException e) {
        if (redirectToRestModelAndView(request)) {
            return handleCustomExceptionRest(e);
        }
        return handleCustomExceptionMVC(e, e.getErrorCode());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(WrongURLException.class)
    public ModelAndView handleWrongURLException(HttpServletRequest request, WrongURLException e) {
        if (redirectToRestModelAndView(request)) {
            return handleCustomExceptionRest(e);
        }
        return handleCustomExceptionMVC(e, e.getErrorCode());
    }


    private boolean redirectToRestModelAndView(HttpServletRequest request) {
        String header = request.getHeader(REST_API_HEADER_KEY);
        return header != null && header.equals(REST_API_HEADER_VALUE);
    }

    private ModelAndView handleCustomExceptionMVC(Exception e, int statusCode) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MVC_ERROR_CODE, statusCode);
        modelAndView.addObject(MVC_ERROR_MESSAGE, e.getMessage());
        modelAndView.setViewName(MVC_ERROR_MAPPING);
        return modelAndView;
    }

    private ModelAndView handleCustomExceptionRest(Exception e) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject(MVC_ERROR_MAPPING, e.getMessage());
        return modelAndView;
    }
}