package com.connectcrew.teamone.userservice.controller;

import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.api.user.auth.Role;
import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.userservice.entity.ProfileEntity;
import com.connectcrew.teamone.userservice.entity.UserEntity;
import com.connectcrew.teamone.userservice.repository.ProfileRepository;
import com.connectcrew.teamone.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/user")
public class AuthController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final Pattern nicknamePattern = Pattern.compile("^[a-zA-Z0-9가-힣]{2,10}$");

    public AuthController(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping("/")
    public Mono<User> find(String socialId, Social provider) {
        log.trace("find socialId={}, provider={}", socialId, provider);
        return userRepository.findBySocialIdAndProvider(socialId, provider.name())
                .switchIfEmpty(Mono.error(new NotFoundException("사용자를 찾을 수 없습니다.")))
                .map(this::entityToResponse);
    }

    @PostMapping("/")
    @Transactional
    public Mono<User> save(@RequestBody UserInputParam input) {
        log.trace("save input={}", input);
        return checkAgreement(input)
                .then(checkNickname(input.nickname()))
                .flatMap(unused -> userRepository.existsBySocialIdAndProvider(input.socialId(), input.provider().name()))
                .flatMap(exists -> {
                    if (exists) return Mono.error(new IllegalArgumentException("이미 존재하는 사용자입니다."));
                    return Mono.just(false);
                })
                .map(exists -> inputToUserEntity(input))
                .flatMap(userRepository::save)
                .flatMap(user -> profileRepository.save(inputToProfileEntity(user.getId(), input)).thenReturn(user))
                .map(this::entityToResponse);
    }

    private Mono<Boolean> checkAgreement(UserInputParam input) {
        if (!input.termsAgreement()) {
            return Mono.error(new IllegalArgumentException("서비스 이용약관에 동의해주세요."));
        } else if (!input.privacyAgreement()) {
            return Mono.error(new IllegalArgumentException("개인정보 처리방침에 동의해주세요."));
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

        return profileRepository.existsByNickname(nickname)
                .flatMap(duplicate -> { // 이름 중복 검사
                    if (duplicate) return Mono.error(new IllegalArgumentException("이미 존재하는 닉네임입니다."));
                    return Mono.just(nickname);
                });
    }

    private UserEntity inputToUserEntity(UserInputParam input) {
        return UserEntity.builder()
                .socialId(input.socialId())
                .provider(input.provider().name())
                .username(input.username())
                .email(input.email())
                .role(Role.USER.name())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .termsAgreement(input.termsAgreement())
                .privacyAgreement(input.privacyAgreement())
                .build();
    }

    private ProfileEntity inputToProfileEntity(Long id, UserInputParam input) {
        return ProfileEntity.builder()
                .userId(id)
                .nickname(input.nickname())
                .profile(input.profile())
                .introduction("")
                .temperature(36.5)
                .recvApply(0)
                .resApply(0)
                .build();
    }

    @NotNull
    private User entityToResponse(UserEntity user) {
        return User.builder()
                .id(user.getId())
                .socialId(user.getSocialId())
                .provider(Social.valueOf(user.getProvider()))
                .username(user.getUsername())
                .email(user.getEmail())
                .role(Role.valueOf(user.getRole()))
                .createdDate(user.getCreatedDate().toString())
                .modifiedDate(user.getModifiedDate().toString())
                .build();
    }
}
