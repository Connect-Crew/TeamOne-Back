package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.api.user.auth.res.UserRes;
import com.connectcrew.teamone.exception.NotFoundException;
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
    public Mono<UserRes> find(String socialId, Social provider) {
        return userRepository.findBySocialIdAndProvider(socialId, provider.name())
                .switchIfEmpty(Mono.error(new NotFoundException("시용자를 찾을 수 없습니다.")))
                .map(this::entityToResponse);
    }

    @PostMapping("/")
    public Mono<UserRes> save(@RequestBody UserInputParam input) {
        return userRepository.findBySocialIdAndProvider(input.socialId(), input.provider().name())
                .defaultIfEmpty(inputToEntity(input))
                .map(entity -> {
                    entity.setUsername(input.username());
                    entity.setNickname(input.nickname());
                    entity.setEmail(input.email());
                    entity.setModifiedDate(LocalDateTime.now().toString());
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
                .email(input.email())
                .role(Role.USER.name())
                .createdDate(LocalDateTime.now().toString())
                .modifiedDate(LocalDateTime.now().toString())
                .build();
    }

    @NotNull
    private UserRes entityToResponse(UserEntity entity) {
        return new UserRes(
                entity.getId(),
                entity.getSocialId(),
                Social.valueOf(entity.getProvider()),
                entity.getUsername(),
                entity.getNickname(),
                entity.getEmail(),
                Role.valueOf(entity.getRole()),
                entity.getCreatedDate(),
                entity.getModifiedDate()
        );
    }

}
