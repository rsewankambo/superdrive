package com.udacity.jwdnd.course1.superdrive.service;

import com.udacity.jwdnd.course1.superdrive.mapper.NoteMapper;
import com.udacity.jwdnd.course1.superdrive.model.Note;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The class handles notes. Annotated with @Service for
 * auto-detection through classpath scannig.
 */
@Service
public class NoteService {
    private NoteMapper noteMapper;
    private UserService userService;
    
    /**
     * Creates a service component to handle note functions.
     *
     * @param noteMapper  a MyBatis mapper for the note table
     * @param userService the Spring Boot user service component
     */
    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }
    
    /**
     * Updates an existing note.
     *
     * @param note the note to be updated
     * @return     the number of updated rows in the database
     */
    public int update(Note note) {
        return noteMapper.updateNote(note);
    }
    
    /**
     * Creates a notes to be saved.
     *
     * @param note a POJO of note data
     * @param auth an Authentication object containing a valid authenticated user
     * @return
     */
    public int create(Note note, Authentication auth) {
        Integer userId = userService.getUser(auth.getName()).getUserId();
        note.setUserId(userId);
        return noteMapper.addNote(note);
    }
    
    /**
     * Retrieve an existing note.
     *
     * @param noteId the ID of the note
     * @return       the credential associated with the given ID
     */
    public Note retrieve(int noteId) {
        return noteMapper.getNote(noteId);
    }
    
    /**
     * Returns all saved notes associated with the current user.
     *
     * @param userId the ID of the note
     * @return       the number of deleted records from the database
     */
    public List<Note> retrieveAll(int userId) {
        return noteMapper.getAllNotes(userId);
    }
    
    /**
     * Deletes an existing note.
     *
     * @param noteId the ID of the note
     * @return       the number of deleted records from the database
     */
    public Integer delete(int noteId) {
        return noteMapper.deleteNote(noteId);
    }
}
