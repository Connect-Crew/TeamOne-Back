package com.connectcrew.teamone.userservice.notification.adapter.out.persistence;

import com.connectcrew.teamone.userservice.notification.adapter.out.persistence.entity.FcmEntity;
import com.connectcrew.teamone.userservice.notification.adapter.out.persistence.repository.FcmRepository;
import com.connectcrew.teamone.userservice.notification.application.port.out.DeleteFcmOutput;
import com.connectcrew.teamone.userservice.notification.application.port.out.FindFcmOutput;
import com.connectcrew.teamone.userservice.notification.application.port.out.SaveFcmOutput;
import com.connectcrew.teamone.userservice.notification.domain.FcmToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class FcmPersistenceAdapter implements SaveFcmOutput, FindFcmOutput, DeleteFcmOutput {

    private final FcmRepository fcmRepository;

    @Override
    public Mono<FcmToken> save(FcmToken token) {
        return fcmRepository.save(FcmEntity.fromDomain(token))
                .map(FcmEntity::toDomain);
    }


    @Override
    public Flux<FcmToken> findAllFcmTokenByUserId(Long userId) {
        return fcmRepository.findAllByUserId(userId)
                .map(FcmEntity::toDomain);
    }

    @Override
    public Mono<Boolean> deleteFcmToken(FcmToken token) {
        return fcmRepository.deleteById(token.id())
                .thenReturn(true);
    }
}
