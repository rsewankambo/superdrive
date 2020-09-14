package com.udacity.jwdnd.course1.superdrive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/")
    public String rootView() { return "redirect:/home"; }
    
    @GetMapping("/login")
    public String loginView() { return "login"; }
}
