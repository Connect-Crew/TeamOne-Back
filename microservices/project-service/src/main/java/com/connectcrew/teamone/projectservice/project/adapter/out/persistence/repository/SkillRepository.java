package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.SkillEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SkillRepository extends ReactiveCrudRepository<SkillEntity, Long> {

    Flux<SkillEntity> findAllByProject(Long project);
}
