package com.connectcrew.teamone.compositeservice.auth.application;

import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.compositeservice.global.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
public class JwtProvider {

    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final Key secret;

    public static final long accessExp = 1000L * 60 * 60 * 24 * 2; // 2 days
    public static final long refreshExp = 1000L * 60 * 60 * 24 * 14; // 14 days

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String createAccessToken(String account, Long id, String nickname, Role role) {
        return createToken(account, id, nickname, role, accessExp);
    }

    public String createRefreshToken(String account, Long id, String nickname, Role role) {
        return createToken(account, id, nickname, role, refreshExp);
    }

    private String createToken(String account, Long id, String nickname, Role role, long expiration) {
        Claims claims = Jwts.claims().setSubject(account);
        claims.put("role", role);
        claims.put("id", id);
        claims.put("nickname", nickname);

        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
    }

    // 권한정보 획득
    // Spring Security 인증과정에서 권한확인을 위한 기능
    public Authentication getAuthentication(String token) throws UnauthorizedException {
        // token에서 bearer 제거
        String removeBearer;
        if (token.startsWith(BEARER_PREFIX))
            removeBearer = token.replace(BEARER_PREFIX, "");
        else
            throw new UnauthorizedException("Invalid token"); // Bearer 없으면 Invalid token

        Claims claims = parseClaims(removeBearer);

        if (claims.get("role") == null) {
            throw new UnauthorizedException("Invalid token");
        }

        Role role = Role.valueOf(claims.get("role").toString());
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.toString()));

        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, removeBearer, authorities);
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

    // 토큰에 담겨있는 유저 username 획득
    public String getAccount(String token) {
        Claims claims = parseClaims(token);

        return claims.getSubject();
    }

    public Role getRole(String token) {
        Claims claims = parseClaims(token);

        return Role.valueOf(claims.get("role").toString());
    }

    public Long getId(String token) {
        Claims claims = parseClaims(token);

        return Long.parseLong(claims.get("id").toString());
    }

    public String getNickname(String token) {
        Claims claims = parseClaims(token);

        return claims.get("nickname").toString();
    }

    // Authorization Header를 통해 인증을 한다.
    public String resolveToken(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey(AUTH_HEADER)) return null;

        return Objects.requireNonNull(request.getHeaders().get(AUTH_HEADER)).stream().findFirst().orElse(null);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = getClaimsJws(token);
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Jws<Claims> getClaimsJws(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
    }
}