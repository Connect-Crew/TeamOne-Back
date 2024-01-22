package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.SkillEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface SkillRepository extends ReactiveCrudRepository<SkillEntity, Long> {

    Flux<SkillEntity> findAllByProject(Long project);

    Flux<SkillEntity> deleteAllByProjectAndIdNotIn(Long project, Collection<Long> ids);
}
