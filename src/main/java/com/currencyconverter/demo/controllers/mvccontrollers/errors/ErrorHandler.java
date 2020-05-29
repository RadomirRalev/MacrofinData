package com.currencyconverter.demo.controllers.mvccontrollers.errors;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorHandler implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Integer statusCode = Integer.valueOf(status.toString());
        model.addAttribute("errorCode", statusCode.toString());
        model.addAttribute("errorMessage", message.toString());
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
