package com.udacity.jwdnd.course1.superdrive.service;

import com.udacity.jwdnd.course1.superdrive.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.superdrive.model.Credential;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

/**
 * This class handles credentials. Annotated with @Service for
 * auto-detection through classpath scannig.
 */
@Service
public class CredentialService {
    private CredentialMapper credMapper;
    private EncryptionService encryptionService;
    private UserService userService;
    
    /**
     * Creates a service component to handle credential functions.
     *
     * @param credMapper        a MyBatis mapper for the credential table
     * @param encryptionService the Spring Boot encryption service component
     * @param userService       the Spring Boot user service component
     */
    public CredentialService(CredentialMapper credMapper, EncryptionService encryptionService, UserService userService) {
        this.credMapper = credMapper;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }
    
    /**
     * Updates an existing credential.
     *
     * @param credential the credential to be updated
     * @return           the number of updated rows in the database
     */
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
    
    /**
     * Creates a new credential to be saved.
     *
     * @param credential a POJO of credential data
     * @param auth       an Authentication object containing a valid authenticated user
     * @return           the number of records inserted into the database
     * @see Authentication
     */
    public int create(Credential credential, Authentication auth) {
        Integer userId = userService.getUser(auth.getName()).getUserId();
        credential.setUserId(userId);
        String userPassword = credential.getPassword();
        if (userPassword != null) encryptUserPassword(credential);
        
        return credMapper.addCredential(credential);
    }
    
    /**
     * Returns an existing credential.
     *
     * @param credentialId the ID of the credential
     * @return             the credential associated with the given ID
     */
    public Credential retrieve(int credentialId) {
        return credMapper.getCredential(credentialId);
    }
    
    /**
     * Returns all saved credentials associated with the current user.
     *
     * @param userId the ID of the currently authenticated user
     * @return       a list of all credentials associated with the current authenticated user
     */
    public List<Credential> retrieveAll(int userId) {
        return credMapper.getAllCredentials(userId);
    }
    
    /**
     * Deletes an existing credential.
     *
     * @param credentialId the ID of the credential
     * @return             the number of deleted records from the database
     */
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
