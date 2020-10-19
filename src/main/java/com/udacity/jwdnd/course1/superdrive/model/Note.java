package com.udacity.jwdnd.course1.superdrive.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Note {
    private Integer noteId;
    private String noteTitle;
    private String noteDescription;
    private Integer userId;
}
