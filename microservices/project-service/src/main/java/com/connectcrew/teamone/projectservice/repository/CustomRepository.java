package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.ProjectCustomEntity;
import com.connectcrew.teamone.projectservice.entity.ProjectCustomFindOption;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

public interface CustomRepository {

    Flux<ProjectCustomEntity> findAllByOption(ProjectCustomFindOption option);
    Flux<Tuple2<Long, String>> findAllProjectIdAndChatIdByUserId(Long userId);
    Flux<Long> findAllMemberIdByUserId(Long projectId);
}
