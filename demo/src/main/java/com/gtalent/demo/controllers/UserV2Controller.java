package com.gtalent.demo.controllers;

import com.gtalent.demo.models.User;
import com.gtalent.demo.repositories.UserRepository;
import com.gtalent.demo.requests.CreateUserRequest;
import com.gtalent.demo.requests.UpdateUserRequest;
import com.gtalent.demo.responses.GetUserResponse;
import com.gtalent.demo.responses.UpdateUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v2/users")
@CrossOrigin("*")
public class UserV2Controller {

    private final UserRepository userRepository;

    @Autowired
    public UserV2Controller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<GetUserResponse>> getAllUser() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users.stream().map(GetUserResponse::new).toList());
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<GetUserResponse> getUserById(@PathVariable int id) {
//        Optional<User> user = userRepository.findById(id);
//        GetUserResponse response = new GetUserResponse(user.get());
//        return ResponseEntity.ok(response);
//    }

    //改寫 : 以Optional包裝，用來避免 null pointer exception
    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            GetUserResponse response = new GetUserResponse(user.get());
            return ResponseEntity.ok(response);
        } else {
            //回傳404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<UpdateUserResponse> updateUserById(@PathVariable int id, @RequestBody UpdateUserRequest request) {
//        Optional<User> user = userRepository.findById(id);
//        User updatedUser = user.get();
//        updatedUser.setUsername(request.getUsername());
//        User savedUser = userRepository.save(updatedUser);
//        GetUserResponse response = new GetUserResponse(savedUser);
//        return ResponseEntity.ok(response);
//    }

    //改寫 : 以Optional包裝，用來避免 null pointer exception。
    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> updateUserById(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        //1.找到User
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            //2.確定找到user之後
            User updatedUser = user.get();
            System.out.println("Before Update:" + updatedUser);
            //3.將欲更新資料填充至對應user
            updatedUser.setUsername(request.getUsername());
            System.out.println("Before Save:" + updatedUser);
            updatedUser = userRepository.save(updatedUser);
            UpdateUserResponse response = new UpdateUserResponse(updatedUser.getUsername());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GetUserResponse> createUsers(@RequestBody CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        System.out.println("Before Save:" + user);
        User savedUser = userRepository.save(user);
        GetUserResponse response = new GetUserResponse(savedUser);
        return ResponseEntity.ok(response);
    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUsersById(@PathVariable int id) {
//        Optional<User> user = userRepository.findById(id);
//        userRepository.delete(user.get());
//        return ResponseEntity.noContent().build();
//    }

    //改寫 : 以Optional包裝，用來避免 null pointer exception。
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsersById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
