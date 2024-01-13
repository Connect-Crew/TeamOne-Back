package com.connectcrew.teamone.projectservice.member.application;

import com.connectcrew.teamone.api.exception.InvalidOwnerException;
import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.projectservice.member.application.port.in.QueryMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.SaveMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.UpdateMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.ApplyCommand;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.SaveMemberCommand;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.UpdateMemberCommand;
import com.connectcrew.teamone.projectservice.member.application.port.in.query.ProjectApplyQuery;
import com.connectcrew.teamone.projectservice.member.application.port.in.query.ProjectApplyStatusQuery;
import com.connectcrew.teamone.projectservice.member.application.port.out.FindMemberOutput;
import com.connectcrew.teamone.projectservice.member.application.port.out.SaveMemberOutput;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.ApplyStatus;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.project.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.projectservice.project.domain.ProjectPart;
import com.connectcrew.teamone.projectservice.project.domain.vo.UserRelationWithProject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAplService implements QueryMemberUseCase, UpdateMemberUseCase, SaveMemberUseCase {

    private final FindProjectOutput findProjectOutput;

    private final FindMemberOutput findMemberOutput;

    private final SaveMemberOutput saveMemberOutput;

    @Override
    public Mono<Member> findMemberByProjectAndUser(Long project, Long user) {
        return findMemberOutput.findByProjectAndUser(project, user);
    }

    @Override
    public Flux<Member> findAllByProject(Long project) {
        return findMemberOutput.findAllByProject(project);
    }

    @Override
    public Mono<UserRelationWithProject> findUserRelationByProjectAndUser(Long projectId, Long userId) {
        Mono<List<MemberPart>> membersParts = findMemberOutput.findByProjectAndUser(projectId, userId)
                .map(m -> m.parts().stream().map(com.connectcrew.teamone.projectservice.member.domain.MemberPart::part).toList())
                .defaultIfEmpty(List.of());

        Mono<List<MemberPart>> applyParts = findMemberOutput.findAllByProjectAndUser(projectId, userId)
                .map(Apply::part)
                .collectList()
                .defaultIfEmpty(List.of());

        return Mono.zip(membersParts, applyParts)
                .map(tuple -> new UserRelationWithProject(projectId, userId, tuple.getT1(), tuple.getT2()));

    }

    @Override
    public Mono<Member> saveMember(SaveMemberCommand command) {
        log.trace("saveMember - command: {}", command);
        return findProjectOutput.findAllProjectPartByProject(command.projectId())
                .collectMap(ProjectPart::part, ProjectPart::id)
                .flatMap(partIdMap -> saveMemberOutput.save(command.toDomain(partIdMap)));
    }

        @Override
        public Mono<Apply> apply(ApplyCommand command) {
            return checkProjectExits(command.projectId())
                    .then(findProjectOutput.findProjectPartByProjectAndPart(command.projectId(), command.part()))
                    .switchIfEmpty(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PART.toString())))
                    .flatMap(this::checkNumberOfMember)
                    .flatMap(part -> checkAlreadyPartMember(part, command.userId()))
                    .flatMap(part -> checkAlreadyApply(part, command.userId()))
                    .flatMap(part -> saveMemberOutput.saveApply(command.toDomain(part.id())));

        }

    private Mono<Boolean> checkProjectExits(Long projectId) {
        return findProjectOutput.existsById(projectId)
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString()));
                    return Mono.just(true);
                });
    }

    private Mono<ProjectPart> checkNumberOfMember(ProjectPart part) {
        if (part.current() >= part.max())
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.COLLECTED_PART.toString()));
        return Mono.just(part);
    }

    /**
     * 이미 해당 part의 member인지 확인
     */
    private Mono<ProjectPart> checkAlreadyPartMember(ProjectPart part, Long userId) {
        return findMemberOutput.existsMemberByPartAndUser(part.id(), userId)
                .flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ALREADY_PART_MEMBER.toString()));
                    return Mono.just(part);
                });
    }

    private Mono<ProjectPart> checkAlreadyApply(ProjectPart part, Long userId) {
        return findMemberOutput.existsApplyByPartAndUser(part.id(), userId)
                .flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ALREADY_APPLY.toString()));
                    return Mono.just(part);
                });
    }

    @Override
    public Mono<Member> updateMember(UpdateMemberCommand command) {
        return findProjectOutput.findById(command.projectId())
                .map(project -> project.parts().stream().collect(Collectors.toMap(ProjectPart::part, ProjectPart::id)))
                .flatMap(partIdMap -> findMemberOutput.findByProjectAndUser(command.projectId(), command.userId())
                        .map(member -> member.update(partIdMap, command.parts()))
                        .flatMap(saveMemberOutput::save)
                );
    }



    @Override
    public Flux<Apply> findAllApplies(ProjectApplyQuery query) {
        return findProjectOutput.findLeaderById(query.projectId())
                .flatMapMany(leader -> {
                    if (!leader.equals(query.leader()))
                        return Flux.error(new InvalidOwnerException("해당 프로젝트의 리더가 아닙니다."));

                    return findMemberOutput.findAllApplyByProjectAndPart(query.projectId(), query.part());
                });
    }

    @Override
    public Flux<ApplyStatus> findAllApplyStatus(ProjectApplyStatusQuery query) {
        return findMemberOutput.findAllApplyByProject(query.projectId())
                .collectList()
                .map(applyList -> applyList.stream().collect(Collectors.groupingBy(Apply::part)))
                .flatMapMany(applyMap -> findProjectOutput.findAllProjectPartByProject(query.projectId())
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
