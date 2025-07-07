package com.syedwajahat01.PasswordManagerProject.model.dto;


import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
}
