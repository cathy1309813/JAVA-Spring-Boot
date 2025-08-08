package com.gtalent.demo.responses;

import com.gtalent.demo.models.User;

public class GetUserResponse {
    private int id;
    private String username;

    public GetUserResponse() {
    }

    public GetUserResponse(int id, String username) {
        this.id = id;
        this.username = username;
    }

    // 新增這個：從 User 轉成 DTO，此寫法是將其包裝成一個user物件(即對外的 DTO)。
    public GetUserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
