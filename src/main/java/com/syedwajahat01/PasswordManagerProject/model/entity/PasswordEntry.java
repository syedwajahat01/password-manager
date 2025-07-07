package com.syedwajahat01.PasswordManagerProject.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "password_entries")
@Data
public class PasswordEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String site;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 1024) // It's good to define a length for encrypted data
    private String encryptedPassword;

    // --- THE FIX for StackOverflowError ---
    // Exclude the back-reference to User to break the recursive loop.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
}