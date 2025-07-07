package com.syedwajahat01.PasswordManagerProject.service;

import com.syedwajahat01.PasswordManagerProject.model.entity.User;
import java.util.Optional;

public interface UserService {

    /**
     * Verifies user credentials and returns the User object if successful.
     * @return An Optional containing the User if credentials are valid, otherwise an empty Optional.
     */
    Optional<User> verifyUser(String username, String password);

    boolean registerUser(String username, String password);

    boolean changeMasterPassword(String username, String oldPassword, String newPassword);
}