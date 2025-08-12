package com.gtalent.demo.Services;

import com.gtalent.demo.models.User;
import com.gtalent.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service  //標示此類別為「服務層」的組件
public class UserService {
    private final UserRepository userRepository;  //將 UserRepository 注入到 UserService

    @Autowired
    //建立 UserService 物件把 UserRepository Bean 注入到這裡，透過 userRepository 去執行資料庫查詢、保存等操作
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);  //findByUsername()方法是自行在UserRepository定義的方法
        if (user.isPresent() && user.get().getPwd().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
}
