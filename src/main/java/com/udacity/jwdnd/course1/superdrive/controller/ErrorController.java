package com.udacity.jwdnd.course1.superdrive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring Boot @controller annotated class to handle application errors.
 */
@Controller
@RequestMapping("/error")
public class ErrorController {
    /**
     * Returns the error page.
     * @return the error page
     */
    public String error() {
        return "error";
    }
}
