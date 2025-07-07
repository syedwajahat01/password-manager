package com.syedwajahat01.PasswordManagerProject.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserChangePasswordRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Old password cannot be blank")
    private String oldPassword;

    @NotBlank(message = "New password cannot be blank")
    private String newPassword;
}