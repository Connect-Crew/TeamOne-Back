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
import com.connectcrew.teamone.projectservice.member.domain.ProjectMemberPart;
import com.connectcrew.teamone.projectservice.notification.application.port.out.SendNotificationOutput;
import com.connectcrew.teamone.projectservice.notification.domain.Notification;
import com.connectcrew.teamone.projectservice.project.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.projectservice.project.application.port.out.UpdateProjectOutput;
import com.connectcrew.teamone.projectservice.project.domain.Project;
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
    private final UpdateProjectOutput updateProjectOutput;

    private final FindMemberOutput findMemberOutput;

    private final SaveMemberOutput saveMemberOutput;
    private final SendNotificationOutput sendNotificationOutput;

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
                .map(m -> m.parts().stream().map(ProjectMemberPart::part).toList())
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
                .flatMap(part -> saveMemberOutput.saveApply(command.toDomain(part.id())))
                .flatMap(this::sendApplyNotificationToLeader)
                .doOnError(ex -> log.error("apply - error: {}", ex.getMessage(), ex));
    }

    private Mono<Apply> sendApplyNotificationToLeader(Apply apply) {
        log.trace("apply - command: {}", apply);
        return findProjectOutput.findById(apply.projectId())
                .doOnNext(project -> sendNotificationOutput.send(
                        new Notification(
                                project.id(),
                                "🔥새로운 지원자 알림",
                                String.format("축하합니다! 지원하신 <b>[%s]</b>팀의 팀원이 되셨습니다!", project.title()),
                                String.format("/apply/project/%d/apply/%d/applier/%d", apply.projectId(), apply.id(), apply.userId())
                        )
                ))
                .thenReturn(apply);
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
                        .map(recruitStatus -> ApplyStatus.of(recruitStatus, (long) applyMap.getOrDefault(recruitStatus.part(), List.of()).size()))
                );
    }

    @Override
    @Transactional
    public Mono<Apply> accept(Long applyId, Long leaderId, String leaderMessage) {
        return findApply(applyId, leaderId)
                .map(apply -> apply.accept(leaderMessage))
                .flatMap(saveMemberOutput::saveApply)
                .flatMap(this::addMember)
                .flatMap(this::sendAcceptedNotificationToApplierAndMembers)
                .flatMap(apply -> addCollectOnPart(apply).map(part -> Tuples.of(apply, part)))
                .flatMap(tuple -> rejectAnotherAppliesIfCollectCompleted(tuple.getT1(), tuple.getT2()));
    }

    private Mono<Apply> sendAcceptedNotificationToApplierAndMembers(Apply apply) {
        return findProjectOutput.findById(apply.projectId())
                .doOnNext(project -> sendNotificationOutput.send(
                        new Notification(
                                apply.userId(),
                                "🎉지원 승인 알림",
                                String.format("축하합니다! 지원하신 <b>[%s]</b>팀의 팀원이 되셨습니다!", project.title()),
                                String.format("/apply/project/%d/apply/%d/applier/%d/accepted", apply.projectId(), apply.id(), apply.userId())
                        )
                ))
                .flatMap(project -> sendNewMemberNotificationToMembers(project, apply))
                .thenReturn(apply);
    }

    private Mono<Apply> sendNewMemberNotificationToMembers(Project project, Apply apply) {
        return findMemberOutput.findAllByProject(apply.projectId())
                .map(Member::user)
                .filter(userId -> !userId.equals(apply.userId()))
                .collect(Collectors.toSet())
                .doOnNext(memberIds -> {
                    for (long memberId : memberIds) {
                        sendNotificationOutput.send(
                                new Notification(
                                        memberId,
                                        "새로운 팀원 알림",
                                        String.format("<b>[%s]</b>팀에 새로운 팀원이 들어왔어요!", project.title()),
                                        String.format("/apply/project/%d/apply/%d/applier/%d/new-member", apply.projectId(), apply.id(), apply.userId())
                                )
                        );
                    }
                })
                .thenReturn(apply);

    }

    private Mono<Apply> rejectAnotherAppliesIfCollectCompleted(Apply apply, ProjectPart part) {
        if (part.current() < part.max()) return Mono.just(apply);

        return findMemberOutput.findAllApplyByProjectAndPart(apply.projectId(), apply.part())
                .map(a -> a.reject("모집이 완료되었습니다."))
                .flatMap(this::sendRejectedNotificationToApplier)
                .collectList()
                .flatMap(saveMemberOutput::saveAllApply)
                .thenReturn(apply);
    }

    private Mono<ProjectPart> addCollectOnPart(Apply apply) {
        return updateProjectOutput.updateCollected(apply.partId(), 1);
    }

    @Override
    public Mono<Apply> reject(Long applyId, Long leaderId, String leaderMessage) {
        return findApply(applyId, leaderId)
                .map(apply -> apply.reject(leaderMessage))
                .flatMap(this::sendRejectedNotificationToApplier)
                .flatMap(saveMemberOutput::saveApply);
    }

    private Mono<Apply> sendRejectedNotificationToApplier(Apply apply) {
        sendNotificationOutput.send(
                new Notification(
                        apply.userId(),
                        "지원 거절 알림",
                        "아쉬워요! 이런 이유로 함께 하지 못했어요.😭 사유 보러 가기!",
                        String.format("/apply/project/%d/apply/%d/user/%d/accepted", apply.projectId(), apply.id(), apply.userId())
                )
        );
        return Mono.just(apply);
    }


    private Mono<Apply> findApply(Long applyId, Long leaderId) {
        return findMemberOutput.findApplyById(applyId)
                .switchIfEmpty(Mono.error(new NotFoundException("해당 지원을 찾을 수 없습니다.")))
                .flatMap(apply -> findProjectOutput.findLeaderById(apply.projectId()).map(leader -> Tuples.of(leader, apply)))
                .switchIfEmpty(Mono.error(new NotFoundException("해당 지원이 있는 프로젝트를 찾을 수 없습니다.")))
                .flatMap(tuple -> {
                    if (!tuple.getT1().equals(leaderId))
                        return Mono.error(new InvalidOwnerException("해당 프로젝트의 리더가 아닙니다."));

                    return Mono.just(tuple.getT2());
                });
    }

    private Mono<Apply> addMember(Apply apply) {
        return findMemberOutput.findByProjectAndUser(apply.projectId(), apply.userId())
                .defaultIfEmpty(new Member(apply.userId(), apply.projectId()))
                .map(member -> member.addPart(apply.part(), apply.partId()))
                .flatMap(saveMemberOutput::save)
                .thenReturn(apply);
    }
}
