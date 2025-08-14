package com.gtalent.demo.controllers;

import com.gtalent.demo.Services.UserService;
import com.gtalent.demo.models.User;
import com.gtalent.demo.repositories.UserRepository;
import com.gtalent.demo.requests.LoginRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/session")
@CrossOrigin("*")
public class SessionAuthController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public SessionAuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request, HttpSession session) {
        Optional<User> user = userService.findByUsernameAndPassword(request.getUsername(), request.getPwd());
        if (user.isPresent()) {
            session.setAttribute("userId", user.get().getId());
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> user = userRepository.findById(userId);
        return ResponseEntity.ok(user.get());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    //新增一個讓使用者可以註冊帳號的API：
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody LoginRequest request, HttpSession session) {
        //1.先接收與驗證資料 並確認 驗證輸入的資料是否合法（例如帳號不為空、密碼不為空）
        if (request.getUsername() == null || request.getPwd() == null) {
            return ResponseEntity.badRequest().build(); //回傳400
        }
        //2.確認資料和否後再執行
        String username = request.getUsername();
        String pwd = request.getPwd();
        //3.檢查帳號是否已存在，避免重複註冊 -->要問資料庫
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); //回傳409
        }
        //4.新使用者存入資料庫
        User newUser =new User();
        newUser.setUsername(username);
        newUser.setPwd(pwd);
        userRepository.save(newUser);
        //5.將使用者登入 session
        session.setAttribute("userId", newUser.getId());
        //6.回傳結果給前端
        return ResponseEntity.status(HttpStatus.CREATED).build(); //回傳201
    }
    
}
