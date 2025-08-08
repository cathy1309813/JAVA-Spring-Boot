package com.gtalent.demo.controllers;


import com.gtalent.demo.models.User;
import com.gtalent.demo.requests.CreateUserRequest;
import com.gtalent.demo.requests.UpdateUserRequest;
import com.gtalent.demo.responses.CreateUserResponse;
import com.gtalent.demo.responses.UserResponse;
import com.gtalent.demo.responses.UpdateUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@CrossOrigin("*")
public class UserV1Controller {

    //由建構子注入: 較具有彈性，可容納更多語法空間
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public UserV1Controller(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {

        String sql = "insert into users (username, email) values(?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, request.getUsername(), request.getEmail());
        if (rowsAffected > 0) {
            CreateUserResponse response = new CreateUserResponse(request.getUsername());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        //user序列化回傳jsom
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser() {
        String sql = "select * from users";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        return ResponseEntity.ok(users.stream().map(UserResponse::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        String sql = "select * from users where id=?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
            System.out.println(user);
            return ResponseEntity.ok(new UserResponse(user));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> updateUserById(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        String sql = "update users set username=? where id=?";
        int rowsAffected = jdbcTemplate.update(sql, request.getUsername());
        if (rowsAffected > 0){
            UpdateUserResponse response = new UpdateUserResponse(request.getUsername());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int id) {
        String sql = "delete from users where id=?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected > 0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUser(@RequestParam String keyword) {
        String sql = "SELECT * FROM users WHERE username LIKE ?";
        String pattern = "%" + keyword + "%";
        List<UserResponse> responses = jdbcTemplate.query(
                sql,
                new Object[]{pattern},
                (rs, rowNum) -> new UserResponse(
                        rs.getInt("id"),
                        rs.getString("username")
                )
        );
        return ResponseEntity.ok(responses);
    }

}
