package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.ReportEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ReportRepository extends ReactiveCrudRepository<ReportEntity, Long> {
    Mono<Boolean> existsByProjectAndUser(Long project, Long user);
}
