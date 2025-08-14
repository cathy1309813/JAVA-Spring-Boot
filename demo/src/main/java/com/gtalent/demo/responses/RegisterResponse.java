package com.gtalent.demo.responses;

import com.gtalent.demo.models.User;

public class RegisterResponse {
    private String username;
    private String email;

    public RegisterResponse() {
    }

    public RegisterResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public RegisterResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
