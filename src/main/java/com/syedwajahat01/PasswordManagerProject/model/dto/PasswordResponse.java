package com.syedwajahat01.PasswordManagerProject.model.dto;


import lombok.Data;

@Data
public class PasswordResponse {
    private String username;
    private String password;

    public PasswordResponse(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
