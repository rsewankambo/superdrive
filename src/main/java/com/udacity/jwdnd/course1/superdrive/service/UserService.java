package com.udacity.jwdnd.course1.superdrive.service;

import com.udacity.jwdnd.course1.superdrive.mapper.UserMapper;
import com.udacity.jwdnd.course1.superdrive.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * This class handles users. Annotated with @Service for
 * auto-detection through classpath scannig.
 */
@Service
public class UserService {
    private UserMapper userMapper;
    private HashService hashService;
    
    /**
     * Creates a service component to handle user functions.
     *
     * @param userMapper  a MyBatis mapper for the user table
     * @param hashService
     */
    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }
    
    /**
     * Returns a boolean as to whether a username already exists in database.
     * @param username a character string representing a username
     * @return         true if username does not exist, false if it does
     */
    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }
    
    /**
     *
     * @param user a POJO of user information
     * @return     the number of records created in the database
     */
    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.addUser(new User(null, user.getUsername(),
                encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }
    
    /**
     * Returns a user associated with the given username.
     * @param username a character string representing a username
     * @return         a POJO of an existing user
     */
    public User getUser(String username) {
        return userMapper.getUser(username);
    }
}
