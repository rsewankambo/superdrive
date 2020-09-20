package com.udacity.jwdnd.course1.superdrive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {
    public String error() {
        return "error";
    }
}
