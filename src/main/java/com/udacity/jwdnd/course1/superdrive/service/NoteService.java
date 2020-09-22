package com.udacity.jwdnd.course1.superdrive.service;

import com.udacity.jwdnd.course1.superdrive.mapper.NoteMapper;
import com.udacity.jwdnd.course1.superdrive.model.Note;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;
    private UserService userService;
    
    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }
    
    public int update(Note note) {
        return noteMapper.updateNote(note);
    }
    
    public int create(Note note, Authentication auth) {
        Integer userId = userService.getUser(auth.getName()).getUserId();
        note.setUserId(userId);
        return noteMapper.addNote(note);
    }
    
    public Note retrieve(int noteId) {
        return noteMapper.getNote(noteId);
    }
    
    public List<Note> retrieveAll(int userId) {
        return noteMapper.getAllNotes(userId);
    }
    
    public Integer delete(int noteId) {
        return noteMapper.deleteNote(noteId);
    }
}
