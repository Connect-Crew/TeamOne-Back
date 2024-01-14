package com.connectcrew.teamone.compositeservice.composite.adapter.out.web;

import com.connectcrew.teamone.api.userservice.favorite.FavoriteType;
import com.connectcrew.teamone.api.userservice.notification.push.SaveFcmTokenApiRequest;
import com.connectcrew.teamone.api.userservice.profile.ProfileApiResponse;
import com.connectcrew.teamone.api.userservice.user.Social;
import com.connectcrew.teamone.api.userservice.user.UserApiResponse;
import com.connectcrew.teamone.api.userservice.user.UserRegisterApiRequest;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.*;
import com.connectcrew.teamone.compositeservice.composite.domain.Profile;
import com.connectcrew.teamone.compositeservice.composite.domain.User;
import com.connectcrew.teamone.compositeservice.global.error.adapter.out.WebClientExceptionHandler;
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
                .bodyToMono(UserApiResponse.class)
                .onErrorResume(exHandler::handleException)
                .map(User::of);
    }

    @Override
    public Mono<Profile> getProfile(Long id) {
        return webClient.get()
                .uri(String.format("%s/profile/?id=%d", host, id))
                .retrieve()
                .bodyToMono(ProfileApiResponse.class)
                .onErrorResume(exHandler::handleException)
                .map(Profile::of);
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
        System.out.printf("userId: %d, type: %s, ids: %s\n", userId, type.name(), ids);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(host[0].trim())
                        .port(Integer.parseInt(host[1].trim()))
                        .path("/favorite/favorites")
                        .queryParam("userId", userId)
                        .queryParam("type", type.name())
                        .queryParam("targets", ids)
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

    @Override
    public Mono<Boolean> saveFcm(Long id, String fcm) {
        return webClient.post()
                .uri(String.format("%s/notification/token", host))
                .bodyValue(new SaveFcmTokenApiRequest(id, fcm))
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<User> save(UserRegisterApiRequest register) {
        return webClient.post()
                .uri(String.format("%s/user/", host))
                .bodyValue(register)
                .retrieve()
                .bodyToMono(User.class)
                .onErrorResume(exHandler::handleException);
    }
}
