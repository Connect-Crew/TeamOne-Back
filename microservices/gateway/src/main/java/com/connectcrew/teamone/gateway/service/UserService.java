package com.connectcrew.teamone.gateway.service;

import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Value("${service.user}")
    private String userServiceHost;

    private final WebClient webClient;

    public UserService() {
        webClient = WebClient.builder().build();
    }

    public Mono<User> getUser(String socialId, Social provider) {
        return webClient.get()
                .uri(String.format("%s/user/?socialId=%s&provider=%s", userServiceHost, socialId, provider.name()))
                .retrieve()
                .bodyToMono(User.class);
                // TODO 에러에 대한 핸들링 구현
    }



}
