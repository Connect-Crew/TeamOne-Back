package com.connectcrew.teamone.projectservice.leader.application;

import com.connectcrew.teamone.api.exception.InvalidOwnerException;
import com.connectcrew.teamone.projectservice.leader.application.port.in.QueryApplyUseCase;
import com.connectcrew.teamone.projectservice.leader.application.port.in.query.ProjectApplyQuery;
import com.connectcrew.teamone.projectservice.leader.application.port.in.query.ProjectApplyStatusQuery;
import com.connectcrew.teamone.projectservice.leader.domain.ApplyStatus;
import com.connectcrew.teamone.projectservice.member.application.port.out.FindMemberOutput;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.project.application.port.out.FindProjectOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderAplService implements QueryApplyUseCase {

    private final FindMemberOutput findMemberOutput;
    private final FindProjectOutput findProjectOutput;

    @Override
    public Flux<Apply> findAllApplies(ProjectApplyQuery query) {
        return findProjectOutput.findLeaderById(query.projectId())
                .flatMapMany(leader -> {
                    if (!leader.equals(query.leader()))
                        return Flux.error(new InvalidOwnerException("해당 프로젝트의 리더가 아닙니다."));

                    return findMemberOutput.findAllApplies(query.projectId(), query.part());
                });
    }

    @Override
    public Flux<ApplyStatus> findAllApplyStatus(ProjectApplyStatusQuery query) {
        return findMemberOutput.findAllAppliesByProject(query.projectId())
                .collectList()
                .map(applyList -> applyList.stream().collect(Collectors.groupingBy(Apply::part)))
                .flatMapMany(applyMap -> findProjectOutput.findAllByProject(query.projectId())
                        .map(recruitStatus -> ApplyStatus.of(recruitStatus, applyMap.getOrDefault(recruitStatus.part(), List.of()).size()))
                );
    }
}
