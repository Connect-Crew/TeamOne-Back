package com.connectcrew.teamone.userservice.repository;

import com.connectcrew.teamone.userservice.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Mono<UserEntity> findBySocialIdAndProvider(String socialId, String provider);
    Mono<Boolean> existsByNickname(String nickname);
    Mono<Boolean> existsBySocialIdAndProvider(String socialId, String provider);
}
