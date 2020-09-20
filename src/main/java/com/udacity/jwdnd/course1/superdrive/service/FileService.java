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

@Service
public class FileService {
    private FileMapper fileMapper;
    private UserService userService;
    
    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }
    
    public File retrieve(int id) {
        return fileMapper.getFileById(id);
    }
    
    public List<File> retrieveAll(int userId) {
        return fileMapper.getAllFiles(userId);
    }
    
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
