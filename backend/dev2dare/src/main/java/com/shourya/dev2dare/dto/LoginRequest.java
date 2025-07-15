package com.shourya.dev2dare.dto;

import javax.validation.constraints.*;

public class LoginRequest {
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
} 