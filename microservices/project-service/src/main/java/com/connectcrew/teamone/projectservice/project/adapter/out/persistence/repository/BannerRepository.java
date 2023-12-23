package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.BannerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BannerRepository extends ReactiveCrudRepository<BannerEntity, Long> {
    Flux<BannerEntity> findAllByProject(Long project);

    Mono<BannerEntity> findFirstByProjectOrderByIdx(Long project);
}
