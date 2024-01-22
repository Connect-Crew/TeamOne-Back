package com.connectcrew.teamone.compositeservice.composite.application;

import com.connectcrew.teamone.api.userservice.favorite.FavoriteType;
import com.connectcrew.teamone.api.userservice.user.Social;
import com.connectcrew.teamone.compositeservice.auth.application.Auth2TokenValidator;
import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.auth.domain.JwtToken;
import com.connectcrew.teamone.compositeservice.auth.domain.TokenClaim;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.AuthUserUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.QueryUserUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.SaveUserUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.RegisterCommand;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.*;
import com.connectcrew.teamone.compositeservice.composite.domain.Profile;
import com.connectcrew.teamone.compositeservice.composite.domain.User;
import com.connectcrew.teamone.compositeservice.composite.domain.vo.LoginResult;
import com.connectcrew.teamone.compositeservice.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAplService implements
        AuthUserUseCase,
        SaveUserUseCase,
        QueryUserUseCase
{
    private final Auth2TokenValidator auth2TokenValidator;
    private final JwtProvider jwtProvider;
    private final SaveUserOutput saveUserOutput;
    private final FindUserOutput findUserOutput;
    private final SaveNotificationOutput saveNotificationOutput;
    private final FindFavoriteOutput findFavoriteOutput;
    private final SaveFavoriteOutput saveFavoriteOutput;

    @Override
    public Mono<LoginResult> login(String auth2Token, Social social, String fcmToken) {
        log.trace("login - auth2Token: {}, social: {}, fcmToken: {}", auth2Token, social, fcmToken);
        return auth2TokenValidator.validate(auth2Token, social)
                .onErrorResume(ex -> Mono.error(new UnauthorizedException("유효하지 않은 Token 입니다.", ex)))
                .doOnError(ex -> log.debug("login error: {}", ex.getMessage(), ex))
                .flatMap(socialId -> findUserOutput.getUser(socialId, social))
                .flatMap(user -> saveNotificationOutput.saveFcm(user.id(), fcmToken).thenReturn(user))
                .flatMap(user -> findUserOutput.getProfile(user.id()).map(profile -> generateLoginResult(user, profile)));
    }

    @Override
    public Mono<LoginResult> register(RegisterCommand command) {
        log.trace("register command={}", command);
        return auth2TokenValidator.validate(command.token(), command.social())
                .onErrorResume(ex -> Mono.error(new UnauthorizedException("유효하지 않은 Token 입니다.", ex)))
                .doOnError(ex -> log.debug("register error: {}", ex.getMessage(), ex))
                .flatMap(socialId -> saveUserOutput.save(command.toApiRequest(socialId)))
                .flatMap(user -> findUserOutput.getProfile(user.id()).map(profile -> generateLoginResult(user, profile)));
    }

    private LoginResult generateLoginResult(User user, Profile profile) {
        JwtToken token = jwtProvider.createToken(user.socialId(), user.id(), profile.nickname(), user.role());
        return LoginResult.from(user, profile, token);
    }

    @Override
    public JwtToken refresh(String refreshToken) {
        TokenClaim claim = jwtProvider.getTokenClaim(refreshToken);

        return jwtProvider.createToken(claim.socialId(), claim.id(), claim.nickname(), claim.role());
    }


    @Override
    public Mono<Boolean> isFavorite(Long userId, FavoriteType type, Long target) {
        return findFavoriteOutput.isFavorite(userId, type, target);
    }

    @Override
    public Mono<Map<Long, Boolean>> isFavorite(Long userId, FavoriteType type, List<Long> ids) {
        return findFavoriteOutput.isFavorite(userId, type, ids);
    }

    @Override
    public Mono<Boolean> setFavorite(Long userId, FavoriteType type, Long target) {
        return saveFavoriteOutput.setFavorite(userId, type, target);
    }
}
