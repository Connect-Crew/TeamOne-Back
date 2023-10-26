package com.connectcrew.teamone.compositeservice.auth;

import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.compositeservice.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
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
public class TokenResolver {
    private final WebClient webClient;

    public TokenResolver() {
        this.webClient = WebClient.builder().build();
    }

    public Mono<Auth2User> resolve(String token, Social social) {
        return switch (social) {
            case GOOGLE -> resolveGoogle(token);
            case KAKAO -> resolveKakao(token);
            case APPLE -> resolveApple(token);
        };
    }

    private Mono<Auth2User> resolveGoogle(String token) {
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";

        ParameterizedTypeReference<Map<String, String>> typeReference = new ParameterizedTypeReference<>() {
        };

        return webClient.get()
                .uri(userInfoEndpoint)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(typeReference)
                .doOnNext(map -> log.info("{}", map))
                .map(map -> new Auth2User(
                        map.get("sub"),
                        map.get("email"),
                        map.get("name"),
                        map.get("picture"),
                        Social.GOOGLE
                ))
                .onErrorResume(e -> Mono.error(new UnauthorizedException(String.format("google token resolve error - %s", e.getMessage()))));
    }

    private Mono<Auth2User> resolveKakao(String token) {
        String kakaoUserInfoEndpoint = "https://kapi.kakao.com/v2/user/me";

        ParameterizedTypeReference<Map<String, Object>> typeReference = new ParameterizedTypeReference<>() {
        };

        return webClient.get()
                .uri(kakaoUserInfoEndpoint)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(typeReference)
                .map(map -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> kakaoAccount = (Map<String, Object>) map.get("kakao_account");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

                    return new Auth2User(
                            (String) map.get("id"),
                            (String) kakaoAccount.get("email"),
                            (String) profile.get("nickname"),
                            (String) profile.get("profile_image_url"),
                            Social.KAKAO
                    );
                })
                .onErrorResume(e -> Mono.error(new UnauthorizedException(String.format("kakao token resolve error - %s", e.getMessage()))));
    }

    private Mono<Auth2User> resolveApple(String token) {
        return null;
//        return getApplePublicKey(Jwts.parserBuilder().build().parse(token).getHeader().get())
//                .flatMap(publicKey -> {
//                    try {
//                        Jws<Claims> verifiedClaims = Jwts.parser()
//                                .setSigningKey(publicKey)
//                                .build()
//                                .parseSignedClaims(token);
//                        Claims body = verifiedClaims.getPayload();
//                        return Mono.just(new Auth2User(
//                                body.getSubject(),
//                                body.get("email", String.class),
//                                body.get("name", String.class),
//                                null,
//                                Social.APPLE
//                        ));
//                    } catch (Exception ex) {
//                        return Mono.error(new UnauthorizedException(String.format("apple token resolve error - %s", ex.getMessage())));
//                    }
//                });
    }

    private Mono<PublicKey> getApplePublicKey(String kid) {
        return webClient.get()
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
