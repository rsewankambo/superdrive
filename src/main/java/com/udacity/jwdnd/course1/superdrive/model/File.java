package com.udacity.jwdnd.course1.superdrive.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class File {
    private Integer fileId;
    private String fileName;
    private String contentType;
    private long fileSize;
    private int userId;
    private byte[] fileData;
    
    public File(Integer fileId, String fileName, String contentType,
                long fileSize, int userId, byte[] fileData) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.userId = userId;
        this.fileData = fileData;
    }
}
