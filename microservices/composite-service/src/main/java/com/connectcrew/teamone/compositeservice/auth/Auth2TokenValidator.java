package com.connectcrew.teamone.compositeservice.auth;

import com.connectcrew.teamone.api.user.auth.Social;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Auth2TokenValidator {
    private final WebClient webClient;

    public Auth2TokenValidator() {
        this.webClient = WebClient.builder().build();
    }

    public Mono<String> validate(String token, Social social) {
        if (token == null || token.isBlank()) {
            return Mono.error(new IllegalArgumentException("Token이 null이거나 비어있습니다."));
        }

        return switch (social) {
            case GOOGLE -> validateGoogle(token);
            case KAKAO -> validateKakao(token);
            case APPLE -> validateApple(token);
        };
    }

    private Mono<String> validateGoogle(String token) {
        log.trace("validateGoogle token={}", token);
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";

        ParameterizedTypeReference<Map<String, String>> typeReference = new ParameterizedTypeReference<>() {
        };

        return webClient.get()
                .uri(userInfoEndpoint)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(typeReference)
                .doOnNext(map -> log.trace("validateGoogle userinfo response={}", map))
                .map(map -> map.get("sub"))
                .onErrorResume(e -> Mono.error(new IllegalArgumentException("유효하지 않은 Token 입니다.", e)));
    }

    private Mono<String> validateKakao(String token) {
        log.trace("validateKakao token={}", token);
        String userInfoEndpoint = "https://kapi.kakao.com/v1/user/access_token_info";

        ParameterizedTypeReference<Map<String, Object>> typeReference = new ParameterizedTypeReference<>() {
        };

        return webClient.get()
                .uri(userInfoEndpoint)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(typeReference)
                .doOnNext(map -> log.trace("validateKakao userinfo response={}", map))
                .map(map -> String.valueOf(map.get("id")))
                .onErrorResume(e -> Mono.error(new IllegalArgumentException("유효하지 않은 Token 입니다.", e)));
    }

    private Mono<String> validateApple(String token) {
        try {
            log.trace("validateApple token={}", token);

            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return Mono.error(new IllegalArgumentException("유효하지 않은 Token 입니다."));
            }

            TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {
            };

            String headerEncoded = parts[0];
            String headerJson = new String(Base64.getUrlDecoder().decode(headerEncoded), StandardCharsets.UTF_8);
            Map<String, Object> header = new ObjectMapper().readValue(headerJson, typeReference);
            String kid = (String) header.get("kid");

            return getApplePublicKey(kid)
                    .flatMap(publicKey -> {
                        try {
                            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                                    .setSigningKey(publicKey)
                                    .build()
                                    .parseClaimsJws(token);

                            Claims body = jwsClaims.getBody();

                            return Mono.just(body.getSubject());
                        } catch (JwtException e) {
                            return Mono.error(new IllegalArgumentException("유효하지 않은 Token 입니다.", e));
                        }
                    });
        } catch (JsonProcessingException ex) {
            return Mono.error(new IllegalArgumentException("유효하지 않은 Token 입니다."));
        }
    }

    public Mono<PublicKey> getApplePublicKey(String kid) {
        log.trace("getApplePublicKey kid={}", kid);
        return webClient.get()
                .uri("https://appleid.apple.com/auth/keys")
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    @SuppressWarnings("unchecked")
                    var keys = (List<Map<String, Object>>) response.get("keys");
                    for (Map<String, Object> keyData : keys) {
                        if (kid.equals(keyData.get("kid"))) {
                            String n = (String) keyData.get("n");
                            String e = (String) keyData.get("e");
                            return Mono.just(createPublicKey(n, e));
                        }
                    }
                    return Mono.error(new RuntimeException("kid " + kid + "에 해당하는 public key를 찾을 수 없습니다."));
                });
    }

    private PublicKey createPublicKey(String n, String e) {
        try {
            byte[] decodedN = Base64.getUrlDecoder().decode(n);
            byte[] decodedE = Base64.getUrlDecoder().decode(e);

            RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(1, decodedN), new BigInteger(1, decodedE));
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException("RSA public key를 생성할 수 없습니다.", ex);
        }
    }

}
