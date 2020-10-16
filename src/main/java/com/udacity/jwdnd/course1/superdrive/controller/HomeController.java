package com.udacity.jwdnd.course1.superdrive.controller;

import com.udacity.jwdnd.course1.superdrive.model.Credential;
import com.udacity.jwdnd.course1.superdrive.model.Note;
import com.udacity.jwdnd.course1.superdrive.model.User;
import com.udacity.jwdnd.course1.superdrive.service.*;
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
    private EncryptionService encryptionService;
    
    public HomeController(FileService fileService,
                          NoteService noteService,
                          CredentialService credentialService,
                          UserService userService,
                          EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }
    
    @GetMapping
    public String homeView(Note noteForm, Credential credentialForm, Authentication authentication, Model model) {
        User user = userService.getUser(authentication.getName());
        int userId = user.getUserId();
        model.addAttribute("userFiles", fileService.retrieveAll(userId));
        model.addAttribute("userNotes", noteService.retrieveAll(userId));
        model.addAttribute("userCredentials", credentialService.retrieveAll(userId));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }
    
}
