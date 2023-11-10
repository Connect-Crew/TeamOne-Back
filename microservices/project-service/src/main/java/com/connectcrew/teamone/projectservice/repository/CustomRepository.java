package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.ProjectCustomEntity;
import com.connectcrew.teamone.projectservice.entity.ProjectCustomFindOption;
import reactor.core.publisher.Flux;

public interface CustomRepository {

    Flux<ProjectCustomEntity> findAllByOption(ProjectCustomFindOption option);
}
