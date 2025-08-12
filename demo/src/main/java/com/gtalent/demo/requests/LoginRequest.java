package com.gtalent.demo.requests;

public class LoginRequest {
    private String username;
    private String pwd;

    public LoginRequest(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
