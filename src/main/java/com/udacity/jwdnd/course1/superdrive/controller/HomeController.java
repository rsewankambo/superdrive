package com.udacity.jwdnd.course1.superdrive.controller;

import com.udacity.jwdnd.course1.superdrive.model.Note;
import com.udacity.jwdnd.course1.superdrive.model.User;
import com.udacity.jwdnd.course1.superdrive.service.CredentialService;
import com.udacity.jwdnd.course1.superdrive.service.FileService;
import com.udacity.jwdnd.course1.superdrive.service.NoteService;
import com.udacity.jwdnd.course1.superdrive.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;
    private User user;
    private int userId;
    
    public HomeController(FileService fileService,
                          NoteService noteService,
                          CredentialService credentialService,
                          UserService userService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
    }
    
    @GetMapping
    public String homeView(Note noteForm, Authentication authentication, Model model) {
        user = userService.getUser(authentication.getName());
        userId = user.getUserId();
        model.addAttribute("userFiles", fileService.retrieveAll(userId));
        model.addAttribute("userNotes", noteService.retrieveAll(userId));
//        model.addAttribute("userCredentials", credentialService.retrieveAll());
        return "home";
    }
    
}
