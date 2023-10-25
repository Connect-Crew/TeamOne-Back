package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.userservice.entity.UserEntity;
import com.connectcrew.teamone.userservice.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class AuthController {

    private final UserRepository userRepository;
    private final Pattern nicknamePattern = Pattern.compile("^[a-zA-Z0-9가-힣]{2,10}$");

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @GetMapping("/")
    public Mono<User> find(String socialId, Social provider) {
        return userRepository.findBySocialIdAndProvider(socialId, provider.name())
                .switchIfEmpty(Mono.error(new NotFoundException("사용자를 찾을 수 없습니다.")))
                .map(this::entityToResponse);
    }

    @PostMapping("/")
    public Mono<User> save(@RequestBody UserInputParam input) {
        return checkAgreement(input)
                .then(checkNickname(input.nickname()))
                .flatMap(unused -> userRepository.existsBySocialIdAndProvider(input.socialId(), input.provider().name()))
                .flatMap(exists -> {
                    if (exists) return Mono.error(new IllegalArgumentException("이미 존재하는 사용자입니다."));
                    return Mono.just(false);
                })
                .map(exists -> inputToEntity(input))
                .flatMap(userRepository::save)
                .map(this::entityToResponse);
    }

    private Mono<Boolean> checkAgreement(UserInputParam input) {
        if (!input.termsAgreement()) {
            return Mono.error(new IllegalArgumentException("서비스 이용약관에 동의해주세요."));
        } else if (!input.privacyAgreement()) {
            return Mono.error(new IllegalArgumentException("개인정보 처리방침에 동의해주세요."));
        } else if (!input.communityPolicyAgreement()) {
            return Mono.error(new IllegalArgumentException("커뮤니티 정책에 동의해주세요."));
        } else {
            return Mono.just(true);
        }
    }

    private Mono<String> checkNickname(String nickname) {
        if (nickname.length() < 2)
            return Mono.error(new IllegalArgumentException("최소 2글자 이상 입력해주세요!"));
        if (nickname.length() > 10)
            return Mono.error(new IllegalArgumentException("최대 10글자 이하로 입력해주세요!"));
        if (!nicknamePattern.matcher(nickname).matches())
            return Mono.error(new IllegalArgumentException("공백과 특수문자는 들어갈 수 없어요."));

        return userRepository.existsByNickname(nickname)
                .flatMap(duplicate -> { // 이름 중복 검사
                    if (duplicate) return Mono.error(new IllegalArgumentException("이미 존재하는 닉네임입니다."));
                    return Mono.just(nickname);
                });
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
                .termsAgreement(input.termsAgreement())
                .privacyAgreement(input.privacyAgreement())
                .communityPolicyAgreement(input.communityPolicyAgreement())
                .adNotificationAgreement(input.adNotificationAgreement())
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
                .adNotifAgree(entity.getAdNotificationAgreement())
                .createdDate(entity.getCreatedDate().toString())
                .modifiedDate(entity.getModifiedDate().toString())
                .build();
    }

}
