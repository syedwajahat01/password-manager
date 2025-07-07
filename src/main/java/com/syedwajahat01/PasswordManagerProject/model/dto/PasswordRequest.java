package com.syedwajahat01.PasswordManagerProject.model.dto;

import lombok.Data;

@Data
public class PasswordRequest {
    private String site;
    private String username;
    private String password; // This can be null or empty to trigger generation


    private Integer length;
    private Boolean includeUppercase;
    private Boolean includeLowercase;
    private Boolean includeNumbers;
    private Boolean includeSymbols;
}