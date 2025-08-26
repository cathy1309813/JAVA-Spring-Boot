package com.gtalent.demo.controllers;

import com.gtalent.demo.Services.AuthService;
import com.gtalent.demo.requests.LoginRequest;
import com.gtalent.demo.requests.RegisterRequest;
import com.gtalent.demo.responses.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
@CrossOrigin("*")
@Tag(name = "JWT驗證", description = "提供使用者登入註冊")
public class JwtAuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "註冊用戶", description = "1.username不得重複。 <br />" +
            "2.密碼必須8個字元以上。 <br />" +
            "3.必須提供用戶角色，並以ROLE_開頭(ROLE_USER)。")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "操作成功"),
            @ApiResponse(responseCode = "400", description = "資料格式不正確"),
            @ApiResponse(responseCode = "403", description = "權限不符")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        //假設檢查必填欄位
        if (request.getUsername() == null || request.getPwd() == null) {
            return ResponseEntity.badRequest().build(); //對應 400
        }
        //假設檢查是否有權限註冊
        if ("admin".equalsIgnoreCase(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //對應 403
        }
        //對應 200
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "登入用戶", description = "返回token")
    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.auth(request));
    }
}
