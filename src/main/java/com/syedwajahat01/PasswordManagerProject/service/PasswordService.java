package com.syedwajahat01.PasswordManagerProject.service;

import com.syedwajahat01.PasswordManagerProject.model.dto.PasswordRequest;
import com.syedwajahat01.PasswordManagerProject.model.dto.PasswordResponse;
import com.syedwajahat01.PasswordManagerProject.model.entity.User;

import java.util.List;

public interface PasswordService {
    PasswordResponse getDecryptedPasswordForUser(String site, User user);
    String addPassword(PasswordRequest request, User sessionUser);
    void deletePassword(String site, User user);
    void deleteAllPasswords(User user);
    List<PasswordResponse> getAllPasswords(User user);
}
