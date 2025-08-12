package com.gtalent.demo.controllers;

import com.gtalent.demo.models.User;
import com.gtalent.demo.requests.CreateUserRequest;
import com.gtalent.demo.requests.UpdateUserRequest;
import com.gtalent.demo.responses.CreateUserResponse;
import com.gtalent.demo.responses.UserResponse;
import com.gtalent.demo.responses.UpdateUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@RestController
@RequestMapping("/users") //將/users提取出來，就不需要在每一個方法裡都需要/users
public class UserController {
    private final Map<Integer, User> mockUser = new HashMap<>();
    //模擬DB自增ID的效果
    protected final AtomicInteger atomicInteger = new AtomicInteger();

    private boolean hasPermissionToDelete(int id) {
        User user = mockUser.get(id);
        if (user == null) {
            System.out.println("找不到用戶 id=" + id);
            return false;
        }
        System.out.println("檢查使用者：" + user.getUsername());
        return true;
    }

    //建構子: 每當UserController被讀取時要作的事情
    public UserController() {
        //假的資料庫
        mockUser.put(1, new User(1, "admin", "admin@gmail.com"));
        mockUser.put(2, new User(2, "user1", "user1@gmail.com"));
        mockUser.put(3, new User(3, "user2", "user2@gmail.com"));
        //老師教學AtomicInteger及getAndIncrement()/incrementAndGet(): 設定自動增加ID起始數字
        atomicInteger.set(4);
    }


//    @GetMapping
//    public List<User> getAllUsers() {
//        List<User> userList = new ArrayList<>(mockUser.values());
//        return userList;
//    }

    //基於資安隱私考量 response id, username(7/29)
    //Map<key, map<key,value>>
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers2() {
        List<User> userList = new ArrayList<>(mockUser.values());
        List<UserResponse> response = new ArrayList<>();
        //遍歷 userList，轉換成 GetUserResponse
        for (User user : userList) {
            UserResponse dto = new UserResponse(user.getId(), user.getUsername());
            response.add(dto);
        }
        return ResponseEntity.ok(response);
    }


//    @GetMapping
//    public ResponseEntity<GetUserResponse> getUserById3(@PathVariable int id) {
//        User user = mockUser.get(id);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        GetUserResponse response = new GetUserResponse();
//        response.setId(user.getId());
//        response.setUsername(user.getUsername());
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }

//    //找特定ID的User: 路徑參數為int id，故URL只要打localhost:8080/users/1就會找到id=1資料
//    @GetMapping("/users/{id}")
//    public User getUserById(@PathVariable int id) {
//        User user = mockUser.get(id);
//        return user;
//    }

//    //回傳404找不到資源
//    @GetMapping("/users/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable int id) {
//        User user = mockUser.get(id);
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
//    }

    //回傳 404找不到資源 及 200 OK
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = mockUser.get(id);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUserById(@PathVariable int id, @RequestBody User request) {
//        User user = mockUser.get(id);
//
//        if(user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        System.out.println(request.getUsername());
//        System.out.println(request.getEmail());
//
//        //update user request body from postman
//        user.setUsername(request.getUsername());
//        user.setEmail(request.getEmail());
//
//        return ResponseEntity.ok(user);
//    }

    //只能更新username(7/29)
    //DTO（Data Transfer Object）分離設計指的是：
    //不直接在 API 的 request 或 response 中使用實體資料模型（如 User），而是設計獨立的資料傳輸物件，用來接收輸入或回傳輸出。
    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> updateUserById2(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        User user = mockUser.get(id);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        System.out.println(request.getUsername());

        user.setUsername(request.getUsername());
        UpdateUserResponse response = new UpdateUserResponse(user.getUsername());
        return ResponseEntity.ok(response);
    }

//    //此方法必須自己編寫id、username、email，不符合資料庫auto_increment邏輯
//    @PostMapping("/users")
//    public ResponseEntity<User> createUser(@RequestBody User request) {
//
//        if(request == null) {
//            //回傳400 Bad Request
//            return ResponseEntity.badRequest().build();
//        }
//        mockUser.put(request.getId(), request);
//        //回傳201 資源已建立
//        return ResponseEntity.status(HttpStatus.CREATED).body(request);
//    }

//    //在系統中，我用一個 AtomicInteger 型別的變數來記錄目前的最大 ID。每當有新的使用者資料要新增時，
//    //就讓這個變數自動加 1，產生一個新的唯一編號，然後把這個編號指派給使用者物件，最後儲存進 Map 裡。
//    //這樣可以模擬資料庫的 auto_increment 行為，確保每筆資料都有獨立不重複的 ID。
//    @PostMapping
//    public ResponseEntity<User> createUser(@RequestBody User request) {
//
//        if(request == null) {
//            //回傳400 Bad Request
//            return ResponseEntity.badRequest().build();
//        }
//        //自動產生 ID
//        int newId = 1; // 預設從 1 開始
//        if (!mockUser.isEmpty()) {
//            int maxId = 0;
//            for (Integer id : mockUser.keySet()) {
//                if (id > maxId) {
//                    maxId = id;
//                }
//            }
//            newId = maxId + 1;
//        }
//        request.setId(newId);
//        mockUser.put(newId, request); //儲存使用者資料到模擬的資料庫
//        return ResponseEntity.status(HttpStatus.CREATED).body(request); //回傳201 資源已建立
//    }

//    //老師教學AtomicInteger及getAndIncrement()/incrementAndGet()
//    @PostMapping
//    //request body反序列化(7/25)
//    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
//        int newId = atomicInteger.getAndIncrement();
//        User user = new User(newId, request.getUserName(), request.getEmail());
//        mockUser.put(newId, user);
//        //user序列化回傳json
//        return new ResponseEntity<>(user, HttpStatus.CREATED);
//    }

    @PostMapping
    //顯示username, email(7/29)
    public ResponseEntity<CreateUserResponse> createUser2(@RequestBody CreateUserRequest request) {
        int newId = atomicInteger.getAndIncrement();
        User user = new User(newId, request.getUsername(), request.getEmail());
        mockUser.put(newId, user);
        //建立只含 username 的回應物件 (排除email資料外洩疑慮)
        CreateUserResponse response = new CreateUserResponse(user.getUsername());
        //回傳只包含 username 的 JSON
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {

        if (id <= 0) {
            //回傳400 Bad Request: ID 為負數或不合法
            return ResponseEntity.badRequest().build();
        }
        if (!hasPermissionToDelete(id)) {
            //回傳403 Forbidden: 沒有權限
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (!mockUser.containsKey(id)) {
            //回傳404 Not Found: 找不到該用戶
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //執行刪除
        mockUser.remove(id);
        return ResponseEntity.noContent().build(); //204 無回應內容，代表刪除成功
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUser(@RequestParam String keyword) {
        List<UserResponse> results = mockUser.values()
                .stream() //lambda起手的表達式
                .filter(user -> //過濾出來符合條件的users
                        user.getUsername().toLowerCase().contains(keyword.toLowerCase()))//結果為true的user[admin]

                //所有結果為true的user[admin] mapping(映射=轉)成 GetUserResponse
                //.map(this::toGetUserResponse) 跟 .map(user -> this.toGetUserResponse(user))這兩種寫法是相同的意思
                .map(UserResponse::new)
                .toList();
//          //改以Lambda: .map(this::toGetUserResponse)去取代第237~240行
//        List<GetUserResponse> responses = new ArrayList<>();
//        for (User user : results) {
//            GetUserResponse response = new GetUserResponse(user.getId(), user.getUsername());
//            responses.add(response);
          return ResponseEntity.ok(results);
//        }
//        return ResponseEntity.ok(responses);
    }

    private UserResponse toGetUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }

    @GetMapping("/normal")
    public ResponseEntity<List<UserResponse>> getNormalUser() {
        List<UserResponse> results = mockUser.values()
                .stream()
                .filter(user -> {
                    String username = user.getUsername();
                    return username != null && !username.trim().equalsIgnoreCase("admin");
                })
                .map(UserResponse::new)
                .toList();

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
    return ResponseEntity.ok(results);
    }
}
