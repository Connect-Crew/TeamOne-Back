package com.connectcrew.teamone.userservice.repository;

import com.connectcrew.teamone.userservice.entity.ProfileEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProfileRepository extends ReactiveCrudRepository<ProfileEntity, Long> {

    Mono<ProfileEntity> findByUserId(Long userId);
}
