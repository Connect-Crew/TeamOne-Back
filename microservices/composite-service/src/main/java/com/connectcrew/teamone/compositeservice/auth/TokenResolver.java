package com.connectcrew.teamone.compositeservice.auth;

import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.compositeservice.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
        return Mono.error(new RuntimeException("Not implemented yet"));
    }
}
