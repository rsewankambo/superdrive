package com.udacity.jwdnd.course1.superdrive.service;

import com.udacity.jwdnd.course1.superdrive.mapper.FileMapper;
import com.udacity.jwdnd.course1.superdrive.model.File;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This class handles file uploads and downloads. Annotated with @Service for
 * auto-detection through classpath scannig.
 */
@Service
public class FileService {
    private FileMapper fileMapper;
    private UserService userService;
    
    /**
     * Creates a service component to handle file functions.
     *
     * @param fileMapper  a MyBatis mapper for the file table
     * @param userService the Spring Boot user service component
     */
    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }
    
    /**
     * Returns and existing file.
     *
     * @param fileId the ID of the file
     * @return       a file associated with the given ID
     */
    public File retrieve(int fileId) {
        return fileMapper.getFileById(fileId);
    }
    
    /**
     * Returns all saved files associated with the current user.
     *
     * @param userId the ID of the currently authenticated user
     * @return       a list of all files associated with the current authenticated user
     */
    public List<File> retrieveAll(int userId) {
        return fileMapper.getAllFiles(userId);
    }
    
    /**
     * Uploads a given file to the database.
     *
     * @param mFile the Multipart file to be parsed for upload to the database
     * @param in    the input stream for the given Multipart file
     * @param auth  an Authentication object containing a valid authenticated user
     * @param model a Model object
     * @return      the number of records inserted into the database
     * @throws IOException
     * @see Authentication
     * @see Model
     */
    public int save(MultipartFile mFile, InputStream in, Authentication auth, Model model) throws IOException {
        long fileSize = mFile.getSize();
        byte[] fileData = in.readAllBytes();
        String contentType = mFile.getContentType();
        String fileName = mFile.getOriginalFilename();
        int userId = userService.getUser(auth.getName()).getUserId();
        
        File file = fileMapper.getFileByName(fileName, userId);
        if (file != null) {
            update(file, fileName, fileSize, fileData);
            model.addAttribute("updateSuccess", true);
            return fileMapper.updateFile(file);
        }
        return fileMapper.addFile(new File(null, fileName, contentType, fileSize, userId, fileData));
    }
    
    private void update(File file, String fileName, long fileSize, byte[] fileData) {
        file.setFileName(fileName);
        file.setFileSize(fileSize);
        file.setFileData(fileData);
    }
    
    public int delete(int fileId) {
        return fileMapper.deleteFile(fileId);
    }
}
