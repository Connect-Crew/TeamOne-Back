package com.connectcrew.teamone.userservice.user.adapter.out.persistence.repository;

import com.connectcrew.teamone.userservice.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Mono<UserEntity> findBySocialIdAndProvider(String socialId, String provider);
    Mono<Boolean> existsBySocialIdAndProvider(String socialId, String provider);
}
