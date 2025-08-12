package com.gtalent.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name="users")  //指定映射到名稱為 "users" 的資料表
public class User {
    @Id
    //auto_increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  //這是主Key
    @Column(name="username")  //代表對應的資料表欄位
    private String username;
    @Column(name="email")  //代表對應的資料表欄位
    private String email;

    @Column(name="pwd")
    //todo 實際應用環境切勿使用明碼儲存(8/12)
    private String pwd;



    public User() {

    }

    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}