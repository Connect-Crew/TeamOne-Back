package com.connectcrew.teamone.projectservice.leader.application.port.in;

import com.connectcrew.teamone.projectservice.leader.application.port.in.query.ProjectApplyQuery;
import com.connectcrew.teamone.projectservice.leader.application.port.in.query.ProjectApplyStatusQuery;
import com.connectcrew.teamone.projectservice.leader.domain.ApplyStatus;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import reactor.core.publisher.Flux;

public interface QueryApplyUseCase {
    Flux<Apply> findAllApplies(ProjectApplyQuery query);

    Flux<ApplyStatus> findAllApplyStatus(ProjectApplyStatusQuery query);
}
