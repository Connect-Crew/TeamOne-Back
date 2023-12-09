package com.connectcrew.teamone.userservice.user.application;

import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.userservice.notification.application.port.out.SaveFcmOutput;
import com.connectcrew.teamone.userservice.profile.application.out.FindProfileOutput;
import com.connectcrew.teamone.userservice.profile.application.out.SaveProfileOutput;
import com.connectcrew.teamone.userservice.user.application.in.CreateUserUseCase;
import com.connectcrew.teamone.userservice.user.application.in.QueryUserUseCase;
import com.connectcrew.teamone.userservice.user.application.in.command.CreateUserCommand;
import com.connectcrew.teamone.userservice.user.application.out.FindUserOutput;
import com.connectcrew.teamone.userservice.user.application.out.SaveUserOutput;
import com.connectcrew.teamone.userservice.user.domain.User;
import com.connectcrew.teamone.userservice.user.domain.enums.Social;
import com.connectcrew.teamone.userservice.user.domain.enums.UserExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAplService implements CreateUserUseCase, QueryUserUseCase {

    private final Pattern nicknamePattern = Pattern.compile("^[a-zA-Z0-9가-힣]{2,10}$");

    private final FindProfileOutput findProfileOutput;
    private final FindUserOutput findUserOutput;
    private final SaveUserOutput saveUserOutput;
    private final SaveProfileOutput saveProfileOutput;
    private final SaveFcmOutput saveFcmOutput;

    @Override
    @Transactional
    public Mono<User> create(CreateUserCommand command) {
        log.trace("save input={}", command);
        return checkAgreement(command)
                .then(checkNickname(command.nickname()))
                .then(checkDuplicateUser(command.socialId(), command.provider()))
                .then(saveUserOutput.save(command.toUserDomain()))
                .flatMap(user -> saveProfileOutput.save(command.toProfileDomain(user.id())).thenReturn(user))
                .flatMap(user -> saveFcmOutput.save(command.toFcmTokenDomain(user.id())).thenReturn(user));
    }

    private Mono<Boolean> checkAgreement(CreateUserCommand command) {
        if (!command.termsAgreement()) {
            return Mono.error(new IllegalArgumentException(UserExceptionMessage.TERMS_AGREEMENT_REQUIRED.getMessage()));
        } else if (!command.privacyAgreement()) {
            return Mono.error(new IllegalArgumentException(UserExceptionMessage.PRIVACY_AGREEMENT_REQUIRED.getMessage()));
        } else {
            return Mono.just(true);
        }
    }

    private Mono<String> checkNickname(String nickname) {
        if (nickname.length() < 2)
            return Mono.error(new IllegalArgumentException(UserExceptionMessage.NAME_LENGTH_LESS_2.getMessage()));
        if (nickname.length() > 10)
            return Mono.error(new IllegalArgumentException(UserExceptionMessage.NAME_LENGTH_OVER_10.getMessage()));
        if (!nicknamePattern.matcher(nickname).matches())
            return Mono.error(new IllegalArgumentException(UserExceptionMessage.SPACE_OR_SPECIAL_CHARACTER_IN_NAME.getMessage()));

        return findProfileOutput.existsByNickname(nickname)
                .flatMap(duplicate -> { // 이름 중복 검사
                    if (duplicate) return Mono.error(new IllegalArgumentException(UserExceptionMessage.DUPLICATE_NICKNAME.getMessage()));
                    return Mono.just(nickname);
                });
    }

    private Mono<Boolean> checkDuplicateUser(String socialId, Social provider) {
        return findUserOutput.existsBySocialIdAndProvider(socialId, provider)
                .flatMap(exists -> {
                    if (exists) return Mono.error(new IllegalArgumentException(UserExceptionMessage.ALREADY_EXISTS_USER.getMessage()));
                    return Mono.just(false);
                });
    }

    @Override
    public Mono<User> findBySocialIdAndProvider(String socialId, String provider) {
        return findUserOutput.findBySocialIdAndProvider(socialId, Social.valueOf(provider))
                .switchIfEmpty(Mono.error(new NotFoundException(UserExceptionMessage.NOTFOUND_USER.getMessage())));
    }
}
