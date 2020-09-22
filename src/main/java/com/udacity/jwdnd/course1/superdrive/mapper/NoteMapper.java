package com.udacity.jwdnd.course1.superdrive.mapper;

import com.udacity.jwdnd.course1.superdrive.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM notes WHERE noteid = #{noteId}")
    Note getNote(int noteId);
    
    @Select("SELECT * FROM notes WHERE userId = #{userId}")
    List<Note> getAllNotes(int userId);
    
    @Insert("INSERT INTO notes(notetitle, notedescription, userid) " +
            "VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int addNote(Note note);
    
    @Update("UPDATE notes SET notetitle = #{noteTitle}, notedescription = #{noteDescription} " +
            "WHERE noteid = #{noteId}")
    int updateNote(Note note);
    
    @Delete("DELETE FROM notes WHERE noteid = #{noteId}")
    int deleteNote(int noteId);
}
