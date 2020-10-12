package com.udacity.jwdnd.course1.superdrive.controller;

import com.udacity.jwdnd.course1.superdrive.model.Note;
import com.udacity.jwdnd.course1.superdrive.service.NoteService;
import com.udacity.jwdnd.course1.superdrive.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {
    private NoteService noteService;
    private UserService userService;
    
    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }
    
    @PostMapping("/note")
    public String saveUserNote(Note noteForm, Authentication auth, Model model) {
        int rows = 0;
        boolean newNote = false;
        if (noteForm.getNoteTitle().isBlank() && noteForm.getNoteDescription().isBlank()) {
            model.addAttribute("emptyNote", true);
        } else {
            if (noteForm.getNoteId() == null) {
                rows = noteService.create(noteForm, auth);
                newNote = true;
            }
            else {
                if (isValidRequest(noteForm, auth)) {
                    rows = noteService.update(noteForm);
                } else {
                    // unauthorized save request
                    model.addAttribute("invalid", true);
                }
            }
            if (!model.containsAttribute("invalid")) {
                if (rows < 1) model.addAttribute("IOError", true);
                else if (newNote) model.addAttribute("createSuccess", true);
                else model.addAttribute("updateSuccess", true);
            }
        }
        model.addAttribute("uploadType", "note");
        return "result";
    }
    
    @GetMapping("/note-delete")
    public String removeUserNote(Integer nid, Authentication auth, Model model) {
        int rowsDeleted = 0;
        if (nid == null) return "error";
        Note note = noteService.retrieve(nid);
    
        if (note != null) {
            if (isValidRequest(note, auth)) {
                rowsDeleted = noteService.delete(nid);
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
        
        model.addAttribute("deleteType", "note");
        return "result";
    }
    
    private boolean isValidRequest(Note note, Authentication auth) {
        int authorizedUserId = note.getUserId();
        int authenticatedUserId = userService.getUser(auth.getName()).getUserId();
        return authenticatedUserId == authorizedUserId;
    }
}
