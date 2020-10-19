package com.udacity.jwdnd.course1.superdrive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Spring Boot @controller annotated class to handle user login http requests.
 */
@Controller
public class LoginController {
    @GetMapping("/")
    public String rootView() { return "redirect:/home"; }
    
    @GetMapping("/login")
    public String loginView() { return "login"; }
}
