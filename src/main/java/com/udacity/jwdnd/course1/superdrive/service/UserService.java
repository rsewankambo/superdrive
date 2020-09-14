package com.udacity.jwdnd.course1.superdrive.service;

import com.udacity.jwdnd.course1.superdrive.mapper.UserMapper;
import com.udacity.jwdnd.course1.superdrive.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private UserMapper userMapper;
    private HashService hashService;
    
    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }
    
    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }
    
    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.addUser(new User(null, user.getUsername(),
                encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }
    
    public User getUser(String username) {
        return userMapper.getUser(username);
    }
    
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }
    
    public int deleteUser(String username) {
        return userMapper.deleteUser(username);
    }
}
