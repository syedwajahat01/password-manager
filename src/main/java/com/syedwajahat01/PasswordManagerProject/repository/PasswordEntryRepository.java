package com.syedwajahat01.PasswordManagerProject.repository;

import com.syedwajahat01.PasswordManagerProject.model.entity.PasswordEntry;
import com.syedwajahat01.PasswordManagerProject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {
    Optional<PasswordEntry> findBySiteAndUser(String site, User user);
    List<PasswordEntry> findAllByUser(User user);
    void deleteBySiteAndUser(String site, User user);
    void deleteAllByUser(User user);
}
