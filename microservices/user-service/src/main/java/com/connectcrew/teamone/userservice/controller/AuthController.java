package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.api.user.auth.res.UserRes;
import com.connectcrew.teamone.exception.NotFoundException;
import com.connectcrew.teamone.userservice.entity.UserEntity;
import com.connectcrew.teamone.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public Mono<UserRes> find(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new NotFoundException("시용자를 찾을 수 없습니다.")))
                .map(user -> new UserRes(
                        user.getId(),
                        user.getUsername(),
                        user.getNickname(),
                        user.getPassword(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedDate(),
                        user.getModifiedDate()
                ));
    }

    @PostMapping("/")
    public Mono<UserRes> save(@RequestBody UserInputParam input) {
        return userRepository.findByUsername(input.username())
                .defaultIfEmpty(UserEntity.builder()
                        .username(input.username())
                        .nickname(input.nickname())
                        .password(input.password())
                        .email(input.email())
                        .role(Role.USER)
                        .createdDate(LocalDateTime.now().toString())
                        .modifiedDate(LocalDateTime.now().toString())
                        .build()
                )
                .map(entity -> {
                    entity.setUsername(input.username());
                    entity.setNickname(input.nickname());
                    entity.setPassword(input.password());
                    entity.setEmail(input.email());
                    entity.setModifiedDate(LocalDateTime.now().toString());
                    return entity;
                })
                .flatMap(userRepository::save)
                .map(entity -> new UserRes(
                        entity.getId(),
                        entity.getUsername(),
                        entity.getNickname(),
                        entity.getPassword(),
                        entity.getEmail(),
                        entity.getRole(),
                        entity.getCreatedDate(),
                        entity.getModifiedDate()
                ));
    }

}
