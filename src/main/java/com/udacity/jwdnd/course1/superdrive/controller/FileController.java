package com.udacity.jwdnd.course1.superdrive.controller;

import com.udacity.jwdnd.course1.superdrive.model.File;
import com.udacity.jwdnd.course1.superdrive.service.FileService;
import com.udacity.jwdnd.course1.superdrive.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class FileController {
    private FileService fileService;
    private UserService userService;
    private ResourceLoader resourceLoader;
    
    public FileController(FileService fileService, UserService userService, ResourceLoader resourceLoader) {
        this.fileService = fileService;
        this.userService = userService;
        this.resourceLoader = resourceLoader;
    }
    
    @PostMapping("/file-upload")
    public String fileUploader(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model) {
        if (fileUpload.isEmpty()) {
            model.addAttribute("noDataError", true);
        } else {
            try (InputStream in = fileUpload.getInputStream();
                 BufferedInputStream bin = new BufferedInputStream(in)) {
    
                int fileId = fileService.save(fileUpload, bin, authentication, model);
                if (fileId < 1) model.addAttribute("IOError", true);
                else {
                    if (!model.containsAttribute("updateSuccess")) {
                        model.addAttribute("uploadSuccess", true);
                    }
                    model.addAttribute("uploadType", "file");
                }
            } catch (IOException e) {
                model.addAttribute("IOError", true);
                e.printStackTrace();
            }
        }
        return "result";
    }
    
    @GetMapping("/download")
    public Object fileDownload(Integer fid, Authentication auth, Model model) {
        if (fid != null) {
            File file = fileService.retrieve(fid);
            if (file != null) {
                if (isRequestValid(file, auth)) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(file.getContentType()))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                            .body(new ByteArrayResource(file.getFileData()));
                }
            }
        }
        // unauthorized download request
        model.addAttribute("invalid", true);
        return "result";
    }
    
    @GetMapping("/file-delete")
    public String removeFile(Integer fid, Authentication auth, Model model) {
        int rowsDeleted = 0;
        if (fid == null) return "error";
        File file = fileService.retrieve(fid);
    
        if (file != null) {
            if (isRequestValid(file, auth)) {
                rowsDeleted = fileService.delete(fid);
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
            // file (file ID) does not exist
            model.addAttribute("invalid", true);
        }
        
        model.addAttribute("deleteType", "file");
        return "result";
    }
    
    private boolean isRequestValid(File file, Authentication auth) {
        int authorizedUserId = file.getUserId();
        int authenticatedUserId = userService.getUser(auth.getName()).getUserId();
        return authenticatedUserId == authorizedUserId;
    }
}
