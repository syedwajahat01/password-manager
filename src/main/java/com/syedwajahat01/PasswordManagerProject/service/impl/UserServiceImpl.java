package com.syedwajahat01.PasswordManagerProject.service.impl;

import com.syedwajahat01.PasswordManagerProject.model.entity.User;
import com.syedwajahat01.PasswordManagerProject.repository.UserRepository;
import com.syedwajahat01.PasswordManagerProject.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    // Correctly inject dependencies via the constructor
    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> verifyUser(String username, String password) {
        return userRepo.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getMasterPasswordHash()));
    }

    @Override
    public boolean registerUser(String username, String password) {
        if (userRepo.findByUsername(username).isPresent()) {
            return false; // User already exists
        }
        User user = new User();
        user.setUsername(username);
        user.setMasterPasswordHash(passwordEncoder.encode(password));
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean changeMasterPassword(String username, String oldPassword, String newPassword) {
        return userRepo.findByUsername(username)
                .filter(user -> passwordEncoder.matches(oldPassword, user.getMasterPasswordHash()))
                .map(user -> {
                    user.setMasterPasswordHash(passwordEncoder.encode(newPassword));
                    userRepo.save(user);
                    return true;
                }).orElse(false);
    }
}