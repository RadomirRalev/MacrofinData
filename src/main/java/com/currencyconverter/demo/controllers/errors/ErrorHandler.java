package com.currencyconverter.demo.controllers.errors;

import com.currencyconverter.demo.exceptions.implementations.WrongURLException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static com.currencyconverter.demo.constants.ControllerConstants.*;
import static com.currencyconverter.demo.constants.ExceptionConstants.WRONG_URL_EXCEPTION_HTTP_CODE;
import static com.currencyconverter.demo.constants.ExceptionConstants.WRONG_URL_EXCEPTION_MESSAGE;


@Controller
public class ErrorHandler implements ErrorController {

    @ResponseBody
    @RequestMapping(MVC_ERROR_MAPPING)
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Integer statusCode = Integer.valueOf(status.toString());
        if (statusCode == WRONG_URL_EXCEPTION_HTTP_CODE) {
            throw new WrongURLException(WRONG_URL_EXCEPTION_MESSAGE);
        }
        model.addAttribute(MVC_ERROR_CODE, statusCode.toString());
        model.addAttribute(MVC_ERROR_MESSAGE, message.toString());
        return MVC_ERROR_MAPPING;
    }

    @Override
    public String getErrorPath() {
        return MVC_ERROR_MAPPING;
    }
}
