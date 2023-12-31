package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository;

import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.ProjectCustomEntity;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectOption;
import reactor.core.publisher.Flux;

public interface CustomRepository {

    Flux<ProjectCustomEntity> findAllByOption(ProjectOption option);

    Flux<ProjectCustomEntity> findAllByUserId(Long userId);
}
