package com.gtalent.demo.configs;

import com.gtalent.demo.Services.JwtService;
import com.gtalent.demo.models.User;
import com.gtalent.demo.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    //驗證流程核心: 基於 OncePerRequest 自定義的一個過濾器 JwtAuthFilter
    //確保每個請求都會經過此過濾器一次

    private final JwtService jwtService;
    private final UserRepository userRepository;

    //依賴注入
    @Autowired
    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    //必須實作繼承類的抽象方法 == 過濾器執行的主要邏輯
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //1.從 http headers 中獲取 Authorization 欄位 -> bearer......
        String authHeader = request.getHeader("Authorization");
        //2.檢查Authorization格式是否正確 : 如果沒有 Header 或不是以 Bearer 開頭 → 當作「未帶 Token」
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //該此請求過濾器結束生命週期 -> 將請求繼續往下傳遞
            filterChain.doFilter(request, response);
            return;
        }
        //3.若開頭格式 (Bearer...) 正確，則擷取第七字元開始的字串 (實際jwt)
        String jwtToken = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(jwtToken);
        //4.驗證使用者
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //去 資料庫(DB) 找到對應的username
            Optional<User> user = userRepository.findByUsername(username);
            //todo 驗證token是否過期或無效
            if (user.isPresent()) {
                //*** 若使用Spring Security(library) 必須包含 授權 (Authorization)邏輯 -> "該用戶能做什麼?" ***
                List<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.get().getRole()));
                //該 token 並非 jwt token ，而是 Spring Security 內部使用的 token (包含 user & authorities)
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user.get(), null, authorities);
//                authenticationToken.setDetails(new WebAuthenticationDetails().buildDetails(request));
                //將 內部使用的token 投進 Spring Security 令牌認證箱 (SecurityContextHolder)
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private List<? extends GrantedAuthority> getUserAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
