package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class UserRequestImpl implements UserRequest {

    public final String host;

    private final WebClient webClient;

    public UserRequestImpl(String host, WebClient webClient) {
        this.host = host;
        this.webClient = webClient;
    }

    public Mono<User> getUser(String socialId, Social provider) {
        return webClient.get()
                .uri(String.format("%s/user/?socialId=%s&provider=%s", host, socialId, provider.name()))
                .retrieve()
                .bodyToMono(User.class);
    }
}
