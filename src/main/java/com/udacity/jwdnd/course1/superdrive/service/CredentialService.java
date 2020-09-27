package com.udacity.jwdnd.course1.superdrive.service;

import com.udacity.jwdnd.course1.superdrive.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.superdrive.model.Credential;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credMapper;
    private EncryptionService encryptionService;
    private UserService userService;
    
    public CredentialService(CredentialMapper credMapper, EncryptionService encryptionService, UserService userService) {
        this.credMapper = credMapper;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }
    
    public int update(Credential credential) {
        String newPassword = credential.getPassword();
        String decryptedStoredPassword = null;
        String storedKey = null;
        String storedPassword = credMapper.getCredential(credential.getCredentialId()).getPassword();
        if (storedPassword != null) {
            storedKey = credMapper.getCredential(credential.getCredentialId()).getKey();
            decryptedStoredPassword = encryptionService.decryptValue(storedPassword, storedKey);
        }
        
        if (!newPassword.equals(decryptedStoredPassword)) {
            encryptUserPassword(credential);
        }
        else {
            credential.setPassword(storedPassword);
            credential.setKey(storedKey);
        }
        
        return credMapper.updateCredential(credential);
    }
    
    public int create(Credential credential, Authentication auth) {
        Integer userId = userService.getUser(auth.getName()).getUserId();
        credential.setUserId(userId);
        String userPassword = credential.getPassword();
        if (userPassword != null) encryptUserPassword(credential);
        
        return credMapper.addCredential(credential);
    }
    
    public Credential retrieve(int credentialId) {
        return credMapper.getCredential(credentialId);
    }
    
    public List<Credential> retrieveAll(int userId) {
        return credMapper.getAllCredentials(userId);
    }
    
    public Integer delete(int credentialId) {
        return credMapper.deleteCredential(credentialId);
    }
    
    private void encryptUserPassword(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
    }
}
