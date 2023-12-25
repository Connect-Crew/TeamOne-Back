package com.connectcrew.teamone.userservice.user.adapter.out.persistence;

import com.connectcrew.teamone.api.user.Social;
import com.connectcrew.teamone.userservice.notification.adapter.out.persistence.repository.FcmRepository;
import com.connectcrew.teamone.userservice.user.adapter.out.persistence.entity.UserEntity;
import com.connectcrew.teamone.userservice.user.adapter.out.persistence.repository.UserRepository;
import com.connectcrew.teamone.userservice.user.application.out.FindUserOutput;
import com.connectcrew.teamone.userservice.user.application.out.SaveUserOutput;
import com.connectcrew.teamone.userservice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements FindUserOutput, SaveUserOutput {

    private final UserRepository userRepository;
    private final FcmRepository fcmTokenRepository;

    @Override
    public Mono<Boolean> existsBySocialIdAndProvider(String socialId, Social provider) {
        return userRepository.existsBySocialIdAndProvider(socialId, provider.name());
    }

    @Override
    public Mono<User> findBySocialIdAndProvider(String socialId, Social provider) {
        return userRepository.findBySocialIdAndProvider(socialId, provider.name())
                .map(UserEntity::toDomain);
    }

    @Override
    public Mono<User> save(User user) {
        return userRepository.save(UserEntity.fromDomain(user))
                .map(UserEntity::toDomain);
    }
}
