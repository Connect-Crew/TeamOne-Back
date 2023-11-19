package com.connectcrew.teamone.userservice.repository;

import com.connectcrew.teamone.userservice.entity.FcmEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FcmRepository extends ReactiveCrudRepository<FcmEntity, Long> {
    Flux<FcmEntity> findAllByUserId(Long userId);

    Mono<FcmEntity> findByUserIdAndToken(Long userId, String token);
}
