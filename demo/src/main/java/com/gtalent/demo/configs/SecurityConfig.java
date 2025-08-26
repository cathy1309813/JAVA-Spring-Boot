package com.gtalent.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//向Spring boot標示該類別為設定檔，Spring將會在啟動時讀取
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                //stateless jwt 用不上csrf
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        //method1:其他沒匹配的請求 不可訪問 --> 安全性高
                        .requestMatchers("/jwt/**").permitAll()
                        .requestMatchers("/session/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v2/users/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/v2/users/**").hasRole("ADMIN")
                        //Spring Security 將會自動加上 ROLE_ -> ROLE_ADMIN
                        .requestMatchers(HttpMethod.GET, "/Suppliers/**").permitAll() //大範圍先寫
                        .requestMatchers("/Suppliers/**").hasRole("SUPPLIER") //小範圍後寫
                        .anyRequest().authenticated() //其他方法都需要驗證

//                        //method2:其他沒匹配的請求 皆可匿名訪問 --> 安全性低
//                        .requestMatchers("/jwt/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/products/**").authenticated()
//                        .requestMatchers(HttpMethod.PUT, "/products/**").authenticated()
//                        .requestMatchers(HttpMethod.DELETE, "/products/**").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/users/**").authenticated()
//                        .requestMatchers(HttpMethod.PUT, "/users/**").authenticated()
//                        .requestMatchers(HttpMethod.DELETE, "/users/**").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/v2/users/**").authenticated()
//                        .requestMatchers(HttpMethod.PUT, "/v2/users/**").authenticated()
//                        .requestMatchers(HttpMethod.DELETE, "/v2/users/**").authenticated()
//                        .anyRequest().permitAll()
                )
                //restful核心: 伺服器無法從session中獲得使用者資訊
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)
                )
                //確保 spring security 進行 UsernamePassword 的驗證以前，我們的jwtAuthFilter會先被執行
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
