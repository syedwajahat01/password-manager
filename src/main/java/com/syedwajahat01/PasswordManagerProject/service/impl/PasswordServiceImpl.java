package com.syedwajahat01.PasswordManagerProject.service.impl;

import com.syedwajahat01.PasswordManagerProject.model.dto.PasswordRequest;
import com.syedwajahat01.PasswordManagerProject.model.dto.PasswordResponse;
import com.syedwajahat01.PasswordManagerProject.model.entity.PasswordEntry;
import com.syedwajahat01.PasswordManagerProject.model.entity.User;
import com.syedwajahat01.PasswordManagerProject.repository.PasswordEntryRepository;
import com.syedwajahat01.PasswordManagerProject.repository.UserRepository;
import com.syedwajahat01.PasswordManagerProject.service.PasswordService;
import com.syedwajahat01.PasswordManagerProject.util.CryptoUtils;
import com.syedwajahat01.PasswordManagerProject.util.PasswordGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final PasswordEntryRepository passwordRepo;
    private final UserRepository userRepo;

    public PasswordServiceImpl(PasswordEntryRepository passwordRepo, UserRepository userRepo) {
        this.passwordRepo = passwordRepo;
        this.userRepo = userRepo;
    }

    private User getFullUser(User sessionUser) {
        return userRepo.findById(sessionUser.getId())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database."));
    }

    @Override
    @Transactional
    public String addPassword(PasswordRequest request, User sessionUser) {
        User fullUser = getFullUser(sessionUser);

        String finalPassword;
        // Check if the provided password is null or just whitespace.
        if (!StringUtils.hasText(request.getPassword())) {
            // --- NEW: Use user-provided options or secure defaults ---
            int length = Optional.ofNullable(request.getLength()).orElse(16); // Default to 16 chars
            boolean useUpper = Optional.ofNullable(request.getIncludeUppercase()).orElse(true);
            boolean useLower = Optional.ofNullable(request.getIncludeLowercase()).orElse(true);
            boolean useNumbers = Optional.ofNullable(request.getIncludeNumbers()).orElse(true);
            boolean useSymbols = Optional.ofNullable(request.getIncludeSymbols()).orElse(true);

            finalPassword = PasswordGenerator.generatePassword(length, useUpper, useLower, useNumbers, useSymbols);
        } else {
            // Otherwise, use the password provided by the user.
            finalPassword = request.getPassword();
        }

        // "Upsert" logic remains the same
        PasswordEntry entry = passwordRepo.findBySiteAndUser(request.getSite(), fullUser)
                .orElse(new PasswordEntry());

        entry.setUser(fullUser);
        entry.setSite(request.getSite());
        entry.setUsername(request.getUsername());
        entry.setEncryptedPassword(CryptoUtils.encrypt(finalPassword, fullUser.getMasterPasswordHash()));
        passwordRepo.save(entry);

        return finalPassword;
    }

    // ... other methods in the class remain the same ...
    @Override
    public PasswordResponse getDecryptedPasswordForUser(String site, User sessionUser) {
        User fullUser = getFullUser(sessionUser);
        PasswordEntry entry = passwordRepo.findBySiteAndUser(site, fullUser)
                .orElseThrow(() -> new RuntimeException("Entry not found for site: " + site));

        String decrypted = CryptoUtils.decrypt(entry.getEncryptedPassword(), fullUser.getMasterPasswordHash());
        return new PasswordResponse(entry.getUsername(), decrypted);
    }

    @Override
    @Transactional
    public void deletePassword(String site, User sessionUser) {
        User fullUser = getFullUser(sessionUser);
        passwordRepo.deleteBySiteAndUser(site, fullUser);
    }

    @Override
    @Transactional
    public void deleteAllPasswords(User sessionUser) {
        User fullUser = getFullUser(sessionUser);
        passwordRepo.deleteAllByUser(fullUser);
    }

    @Override
    public List<PasswordResponse> getAllPasswords(User sessionUser) {
        User fullUser = getFullUser(sessionUser);
        return passwordRepo.findAllByUser(fullUser).stream()
                .map(entry -> {
                    String decryptedPassword = CryptoUtils.decrypt(entry.getEncryptedPassword(), fullUser.getMasterPasswordHash());
                    return new PasswordResponse(entry.getUsername(), decryptedPassword);
                })
                .collect(Collectors.toList());
    }
}