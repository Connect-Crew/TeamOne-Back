package com.connectcrew.teamone.projectservice.member.application;

import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.projectservice.member.application.port.in.QueryMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.UpdateMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.ApplyCommand;
import com.connectcrew.teamone.projectservice.member.application.port.out.FindMemberOutput;
import com.connectcrew.teamone.projectservice.member.application.port.out.SaveMemberOutput;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.project.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAplService implements QueryMemberUseCase, UpdateMemberUseCase {

    private final FindProjectOutput findProjectOutput;

    private final FindMemberOutput findMemberOutput;

    private final SaveMemberOutput saveMemberOutput;

    @Override
    public Mono<List<Member>> findAllByProject(Long project) {
        log.trace("findAllByProject - project: {}", project);
        return findProjectOutput.findById(project)
                .flatMap(p -> findMemberOutput.findAllByProject(project)
                        .map(members -> members.stream()
                                .map(member -> member.setLeader(Objects.equals(p.leader(), member.memberId())))
                                .toList()));
    }

    @Override
    public Mono<Boolean> apply(ApplyCommand command) {
        return checkProjectExits(command.projectId())
                .then(findProjectOutput.findByProjectAndPart(command.projectId(), command.part()))
                .switchIfEmpty(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PART.toString())))
                .flatMap(this::checkNumberOfMember)
                .flatMap(recruit -> checkAlreadyPartMember(recruit, command.userId()))
                .flatMap(recruit -> checkAlreadyApply(recruit, command.userId()))
                .flatMap(recruit -> saveMemberOutput.saveApply(command.toDomain(recruit.id())))
                .thenReturn(true);

    }

    private Mono<Boolean> checkProjectExits(Long projectId) {
        return findProjectOutput.existsProjectById(projectId)
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString()));
                    return Mono.just(true);
                });
    }

    private Mono<RecruitStatus> checkNumberOfMember(RecruitStatus recruitStatus) {
        if (recruitStatus.current() >= recruitStatus.max())
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.COLLECTED_PART.toString()));
        return Mono.just(recruitStatus);
    }

    /**
     * 이미 해당 part의 member인지 확인
     */
    private Mono<RecruitStatus> checkAlreadyPartMember(RecruitStatus recruitStatus, Long userId) {
        return findMemberOutput.existsMemberByPartAndUser(recruitStatus.id(), userId)
                .flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ALREADY_PART_MEMBER.toString()));
                    return Mono.just(recruitStatus);
                });
    }

    private Mono<RecruitStatus> checkAlreadyApply(RecruitStatus recruitStatus, Long userId) {
        return findMemberOutput.existsApplyByPartAndUser(recruitStatus.id(), userId)
                .flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ALREADY_APPLY.toString()));
                    return Mono.just(recruitStatus);
                });
    }
}
