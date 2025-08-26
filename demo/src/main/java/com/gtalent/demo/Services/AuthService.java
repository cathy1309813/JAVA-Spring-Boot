package com.gtalent.demo.Services;


import com.gtalent.demo.models.User;
import com.gtalent.demo.repositories.UserRepository;
import com.gtalent.demo.requests.LoginRequest;
import com.gtalent.demo.requests.RegisterRequest;
import com.gtalent.demo.responses.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        //註冊請求 (RegisterRequest) 取得新使用者資料
        User newUser = new User();
        //取得新使用者 username、email、pwd
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPwd(passwordEncoder.encode(request.getPwd()));
        newUser.setRole(request.getRole());
        //將新使用者資料存進資料庫
        userRepository.save(newUser);
        //產生 JWT Token
        String jwtToken = jwtService.generateToken(newUser);
        //回傳 Token 給前端
        return new AuthResponse(jwtToken);
    }

    public AuthResponse auth(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(request.getPwd(), user.getPwd())) {
                String jwtToken = jwtService.generateToken(user);
                return new AuthResponse(jwtToken);
            }
        }
        throw new RuntimeException("無效的憑證");
    }
}
