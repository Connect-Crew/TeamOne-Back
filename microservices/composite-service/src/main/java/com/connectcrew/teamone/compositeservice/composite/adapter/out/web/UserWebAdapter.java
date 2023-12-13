package com.connectcrew.teamone.compositeservice.composite.adapter.out.web;

import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.api.user.favorite.FavoriteType;
import com.connectcrew.teamone.api.user.notification.FcmToken;
import com.connectcrew.teamone.api.user.profile.Profile;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.*;
import com.connectcrew.teamone.compositeservice.global.exception.WebClientExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserWebAdapter implements SaveUserOutput, FindUserOutput, FindFavoriteOutput, SaveFavoriteOutput, SaveNotificationOutput {

    @Value("${app.user}")
    public String host;

    private final WebClient webClient;

    private final WebClientExceptionHandler exHandler;

    @Override
    public Mono<User> getUser(String socialId, Social provider) {
        return webClient.get()
                .uri(String.format("%s/user/?socialId=%s&provider=%s", host, socialId, provider.name()))
                .retrieve()
                .bodyToMono(User.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<User> saveUser(UserInputParam user) {
        return webClient.post()
                .uri(String.format("%s/user/", host))
                .bodyValue(user)
                .retrieve()
                .bodyToMono(User.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Profile> getProfile(Long id) {
        return webClient.get()
                .uri(String.format("%s/profile/?id=%d", host, id))
                .retrieve()
                .bodyToMono(Profile.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Boolean> isFavorite(Long userId, FavoriteType type, Long target) {
        return webClient.get()
                .uri(String.format("%s/favorite/?userId=%d&type=%s&target=%d", host, userId, type.name(), target))
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Map<Long, Boolean>> isFavorite(Long userId, FavoriteType type, List<Long> ids) {
        String[] host = this.host.replace("http://", "").split(":");
        ParameterizedTypeReference<Map<Long, Boolean>> typeRef = new ParameterizedTypeReference<>() {
        };
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(host[0].trim())
                        .port(Integer.parseInt(host[1].trim()))
                        .path("/favorite/favorites")
                        .queryParam("userId", userId)
                        .queryParam("type", type.name())
                        .queryParam("ids", ids)
                        .build())
                .retrieve()
                .bodyToMono(typeRef)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Boolean> setFavorite(Long userId, FavoriteType type, Long target) {
        return webClient.post()
                .uri(String.format("%s/favorite/?userId=%d&type=%s&target=%d", host, userId, type.name(), target))
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }

//    @Override
//    public Mono<Boolean> sendNotification(FcmNotification notification) {
//        return webClient.post()
//                .uri(String.format("%s/notification", host))
//                .bodyValue(notification)
//                .retrieve()
//                .bodyToMono(Boolean.class)
//                .onErrorResume(exHandler::handleException);
//    }

    @Override
    public Mono<Boolean> saveFcm(Long id, String fcm) {
        return webClient.post()
                .uri(String.format("%s/notification/token", host))
                .bodyValue(new FcmToken(id, fcm))
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }
}
