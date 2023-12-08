package com.connectcrew.teamone.chatservice.chat.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtValidator {
    public static final String BEARER_PREFIX = "Bearer ";

    private final Key secret;

    public JwtValidator(@Value("${jwt.secret}") String secret) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = getClaimsJws(removeBearerPrefix(token));
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private String removeBearerPrefix(String token) {
        return token.replace(BEARER_PREFIX, "");
    }

    public Jws<Claims> getClaimsJws(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
    }

    public Long getId(String token) {
        Claims claims = parseClaims(token);

        return Long.parseLong(claims.get("id").toString());
    }

    public String getNickname(String token) {
        Claims claims = parseClaims(token);

        return claims.get("nickname").toString();
    }


    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims();
        }
    }
}
