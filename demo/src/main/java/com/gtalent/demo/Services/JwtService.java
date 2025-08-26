package com.gtalent.demo.Services;

import com.gtalent.demo.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Service
public class JwtService {

    private long EXPIRATION_TIME; //毫秒

    public String generateToken(User user) {
        return buildToken(user);
    }

    public String buildToken(User user) {
        return Jwts.builder()
                //唯一的使用者名稱
                .setSubject(user.getUsername())
                //發行時間
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //過期時間
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) //24小時
                //對jwt進行簽名
                .signWith(getKey(), SignatureAlgorithm.HS256)
                //組合成字串
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode("dGlueXNhbWVoYW5kc29tZXlvdW5nY2FsbHJlY29yZGdpZnRpbnZlbnRlZHdpdGhvdXQ=");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey()).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
