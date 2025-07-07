package com.syedwajahat01.PasswordManagerProject.controller;

import com.syedwajahat01.PasswordManagerProject.model.dto.*;
import com.syedwajahat01.PasswordManagerProject.model.entity.User;
import com.syedwajahat01.PasswordManagerProject.service.PasswordService;
import com.syedwajahat01.PasswordManagerProject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class VaultController {

    private final PasswordService passwordService;
    private final UserService userService;

    // This repository is responsible for saving the security context to the session.
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public VaultController(PasswordService passwordService, UserService userService) {
        this.passwordService = passwordService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterRequest request) {
        boolean created = userService.registerUser(request.getUsername(), request.getPassword());
        return created
                ? ResponseEntity.ok("User registered.")
                : ResponseEntity.status(HttpStatus.CONFLICT).body("Username exists");
    }

    @PostMapping("/unlock")
    public ResponseEntity<String> unlock(@RequestBody UserLoginRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // 1. Use the refactored service method which returns the User object
        Optional<User> userOpt = userService.verifyUser(request.getUsername(), request.getPassword());

        if (userOpt.isPresent()) {
            User user = userOpt.get(); // This user object now implements UserDetails

            // 2. Create an authentication token for Spring Security
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    user.getPassword(), // Credentials
                    user.getAuthorities() // Roles/Permissions
            );

            // 3. Create a new security context, set the authentication
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // 4. This crucial step saves the context to the HTTP session for subsequent requests
            securityContextRepository.saveContext(context, httpServletRequest, httpServletResponse);

            return ResponseEntity.ok("Vault unlocked.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Logged out successfully.");
    }

    // --- PROTECTED ENDPOINTS ---
    // These will now work correctly because the user is properly authenticated.

    @PostMapping("/passwords")
    public ResponseEntity<?> addPassword(@RequestBody PasswordRequest request, @AuthenticationPrincipal User user) {
        // The service now returns the password that was used.
        String passwordUsed = passwordService.addPassword(request, user);

        // Return a helpful response object that includes the password.
        return ResponseEntity.ok(
                // Using Map.of for a simple, inline JSON object response
                Map.of(
                        "message", "Password for site '" + request.getSite() + "' has been saved.",
                        "passwordUsed", passwordUsed
                )
        );
    }

    @GetMapping("/passwords")
    public ResponseEntity<?> getPassword(@RequestParam String site, @AuthenticationPrincipal User user) {
        try {
            PasswordResponse response = passwordService.getDecryptedPasswordForUser(site, user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/passwords/all")
    public ResponseEntity<List<PasswordResponse>> getAllPasswords(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(passwordService.getAllPasswords(user));
    }

    @DeleteMapping("/passwords")
    public ResponseEntity<String> deletePassword(@RequestParam String site, @AuthenticationPrincipal User user) {
        passwordService.deletePassword(site, user);
        return ResponseEntity.ok("Password deleted.");
    }

    @DeleteMapping("/passwords/all")
    public ResponseEntity<String> deleteAll(@AuthenticationPrincipal User user) {
        passwordService.deleteAllPasswords(user);
        return ResponseEntity.ok("All passwords deleted.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody UserChangePasswordRequest request, // Add @Valid here
            @AuthenticationPrincipal User sessionUser,
            HttpServletRequest httpServletRequest) {

        if (!sessionUser.getUsername().equals(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden: User mismatch.");
        }

        boolean changed = userService.changeMasterPassword(
                request.getUsername(),
                request.getOldPassword(),
                request.getNewPassword()
        );

        if (changed) {
            logout(httpServletRequest); // Force re-login for security
            return ResponseEntity.ok("Password changed successfully. Please log in again.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not change password. Invalid current password.");
        }
    }
}