package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.userservice.entity.UserEntity;
import com.connectcrew.teamone.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public Mono<User> find(String socialId, Social provider) {
        return userRepository.findBySocialIdAndProvider(socialId, provider.name())
                .map(this::entityToResponse);
    }

    @PostMapping("/")
    public Mono<User> save(@RequestBody UserInputParam input) {
        return userRepository.findBySocialIdAndProvider(input.socialId(), input.provider().name())
                .defaultIfEmpty(inputToEntity(input))
                .map(entity -> {
                    entity.setUsername(input.username());
                    entity.setNickname(input.nickname());
                    entity.setEmail(input.email());
                    entity.setProfile(input.profile());
                    entity.setModifiedDate(LocalDateTime.now());
                    return entity;
                })
                .flatMap(userRepository::save)
                .map(this::entityToResponse);
    }

    private UserEntity inputToEntity(UserInputParam input) {
        return UserEntity.builder()
                .socialId(input.socialId())
                .provider(input.provider().name())
                .username(input.username())
                .nickname(input.nickname())
                .profile(input.profile())
                .email(input.email())
                .role(Role.USER.name())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    @NotNull
    private User entityToResponse(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .socialId(entity.getSocialId())
                .provider(Social.valueOf(entity.getProvider()))
                .username(entity.getUsername())
                .nickname(entity.getNickname())
                .profile(entity.getProfile())
                .email(entity.getEmail())
                .role(Role.valueOf(entity.getRole()))
                .createdDate(entity.getCreatedDate().toString())
                .modifiedDate(entity.getModifiedDate().toString())
                .build();
    }

}
