package com.udacity.jwdnd.course1.superdrive.controller;

import com.udacity.jwdnd.course1.superdrive.model.Credential;
import com.udacity.jwdnd.course1.superdrive.service.CredentialService;
import com.udacity.jwdnd.course1.superdrive.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;
    
    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }
    
    @PostMapping("/credential")
    public String saveUserCredential(Credential credentialForm, Authentication auth, Model model) {
        int rows = 0;
        if (credentialForm.getUrl().isBlank() && credentialForm.getUsername().isBlank() && credentialForm.getUsername().isBlank()) {
            model.addAttribute("emptyCredential", true);
        } else {
            if (credentialForm.getCredentialId() == null) {
                rows = credentialService.create(credentialForm, auth);
            }
            else {
                if (isValidRequest(credentialForm, auth)) {
                    rows = credentialService.update(credentialForm);
                } else {
                    // unauthorized save request
                    model.addAttribute("invalid", true);
                }
            }
            if (!model.containsAttribute("invalid")) {
                if (rows < 1) model.addAttribute("IOError", true);
                else model.addAttribute("updateSuccess", true);
            }
        }
        model.addAttribute("uploadType", "credential");
        return "result";
    }
    
    @GetMapping("/cred-delete")
    public String removeUserCredential(Integer cid, Authentication auth, Model model) {
        int rowsDeleted = 0;
        if (cid == null) return "error";
        Credential credential = credentialService.retrieve(cid);
        
        if (credential != null) {
            if (isValidRequest(credential, auth)) {
                rowsDeleted = credentialService.delete(cid);
                if (rowsDeleted == 0) {
                    // error during database delete
                    model.addAttribute("deleteError", true);
                } else {
                    model.addAttribute("deleteSuccess", true);
                }
            } else {
                // unauthorized delete request
                model.addAttribute("invalid", true);
            }
        } else {
            // note (note ID) does not exist
            model.addAttribute("invalid", true);
        }
        
        model.addAttribute("deleteType", "credential");
        return "result";
    }
    
    private boolean isValidRequest(Credential credential, Authentication auth) {
        int authorizedUserId = credential.getUserId();
        int authenticatedUserId = userService.getUser(auth.getName()).getUserId();
        return authenticatedUserId == authorizedUserId;
    }
}
