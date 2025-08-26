package com.gtalent.demo.controllers;

import com.gtalent.demo.models.User;
import com.gtalent.demo.repositories.UserRepository;
import com.gtalent.demo.requests.CreateUserRequest;
import com.gtalent.demo.requests.UpdateUserRequest;
import com.gtalent.demo.responses.UserResponse;
import com.gtalent.demo.responses.UpdateUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "使用者管理-第二版", description = "提供使用者管理的第二版 API")
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
    @Operation(summary = "取得所有用戶",
               description = "1.回傳系統中所有已註冊的使用者清單。<br />" +
                       "2.此 API 不需要提供 JWT Token。")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功取得使用者清單"),
            @ApiResponse(responseCode = "400", description = "輸入錯誤"),
            @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    })
    public ResponseEntity<List<UserResponse>> getAllUser() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users.stream().map(UserResponse::new).toList());
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<GetUserResponse> getUserById(@PathVariable int id) {
//        Optional<User> user = userRepository.findById(id);
//        GetUserResponse response = new GetUserResponse(user.get());
//        return ResponseEntity.ok(response);
//    }

    //改寫 : 以Optional包裝，用來避免 null pointer exception
    @GetMapping("/{id}")
    @Operation(summary = "取得單一用戶",
            description = "1.根據使用者 ID 取得該用戶的詳細資訊。 <br />" +
                    "2.此 API 不需要提供 JWT Token。")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功取得使用者",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "找不到該使用者"),
            @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserResponse response = new UserResponse(user.get());
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
    @Operation(summary = "更新單一用戶名稱",
            description = "1.根據使用者 ID 更新該使用者名稱，成功後回傳更新後的名稱。 <br />" +
                    "2.此 API 需要提供有效的 JWT Bearer Token。",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "更新成功",
                    content = @Content(schema = @Schema(implementation = UpdateUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "輸入資料格式錯誤"),
            @ApiResponse(responseCode = "404", description = "找不到該使用者"),
            @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    })
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
    @Operation(summary = "創建新用戶名稱及電子郵件",
            description = "1.創建新使用者名稱及電子郵件，並自動新增ID。 <br />" +
                    "2.此 API 需要提供有效的 JWT Bearer Token。",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "創建成功",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "輸入資料格式錯誤"),
            @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    })
    public ResponseEntity<UserResponse> createUsers(@RequestBody CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        System.out.println("Before Save:" + user);
        User savedUser = userRepository.save(user);
        UserResponse response = new UserResponse(savedUser);
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
    @Operation(summary = "刪除用戶",
            description = "1.刪除使用者。 <br />" +
                    "2.此 API 需要提供有效的 JWT Bearer Token。",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "刪除成功，無回傳內容"),
            @ApiResponse(responseCode = "400", description = "輸入資料格式錯誤"),
            @ApiResponse(responseCode = "404", description = "找不到該使用者"),
            @ApiResponse(responseCode = "500", description = "伺服器內部錯誤")
    })
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
