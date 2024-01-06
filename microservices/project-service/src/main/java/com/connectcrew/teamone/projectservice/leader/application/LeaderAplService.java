package com.connectcrew.teamone.projectservice.leader.application;

import com.connectcrew.teamone.api.exception.InvalidOwnerException;
import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.projectservice.leader.application.port.in.QueryApplyUseCase;
import com.connectcrew.teamone.projectservice.leader.application.port.in.UpdateApplyUseCase;
import com.connectcrew.teamone.projectservice.leader.application.port.in.query.ProjectApplyQuery;
import com.connectcrew.teamone.projectservice.leader.application.port.in.query.ProjectApplyStatusQuery;
import com.connectcrew.teamone.projectservice.leader.domain.ApplyStatus;
import com.connectcrew.teamone.projectservice.member.application.port.out.FindMemberOutput;
import com.connectcrew.teamone.projectservice.member.application.port.out.SaveMemberOutput;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.project.application.port.out.FindProjectOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderAplService implements QueryApplyUseCase, UpdateApplyUseCase {

    private final FindMemberOutput findMemberOutput;
    private final SaveMemberOutput saveMemberOutput;
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

    @Override
    @Transactional
    public Mono<Apply> accept(Long applyId, Long leaderId) {
        return findMemberOutput.findApplyById(applyId)
                .switchIfEmpty(Mono.error(new NotFoundException("해당 지원을 찾을 수 없습니다.")))
                .flatMap(apply -> findProjectOutput.findLeaderById(apply.projectId()).map(leader -> Tuples.of(leader, apply)))
                .switchIfEmpty(Mono.error(new NotFoundException("해당 지원이 있는 프로젝트를 찾을 수 없습니다.")))
                .flatMap(tuple -> {
                    if (!tuple.getT1().equals(leaderId))
                        return Mono.error(new InvalidOwnerException("해당 프로젝트의 리더가 아닙니다."));

                    return Mono.just(tuple.getT2());
                })
                .flatMap(saveMemberOutput::saveApply);
                // TODO UPDATE APPLY STATUS AND ADD MEMBER
    }

    @Override
    public Mono<Apply> reject(Long applyId, Long leaderId) {
        return findMemberOutput.findApplyById(applyId)
                .switchIfEmpty(Mono.error(new NotFoundException("해당 지원을 찾을 수 없습니다.")))
                .flatMap(apply -> findProjectOutput.findLeaderById(apply.projectId()).map(leader -> Tuples.of(leader, apply)))
                .switchIfEmpty(Mono.error(new NotFoundException("해당 지원이 있는 프로젝트를 찾을 수 없습니다.")))
                .flatMap(tuple -> {
                    if (!tuple.getT1().equals(leaderId))
                        return Mono.error(new InvalidOwnerException("해당 프로젝트의 리더가 아닙니다."));

                    return Mono.just(tuple.getT2());
                });
                // TODO UPDATE APPLY STATUS
    }
}
