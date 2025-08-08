package com.gtalent.demo.responses;

public class UpdateUserResponse {
    private String username;

    //無參構造器: 為了支援未來擴充性（如 JSON 序列化、測試或使用某些框架）
    public UpdateUserResponse() {
    }

    public UpdateUserResponse(String username) {
        this.username = username;
    }

    // Getter, Setter

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
