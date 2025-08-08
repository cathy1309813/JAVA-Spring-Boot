package com.gtalent.demo.requests;

public class UpdateUserRequest {
    private String username;

    //無參構造器: 必要於反序列化
    public UpdateUserRequest() {
    }

    //有參構造器: 可用於手動建立物件
    public UpdateUserRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
