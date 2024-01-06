package com.connectcrew.teamone.projectservice.member.adapter.in.web;

import com.connectcrew.teamone.api.projectservice.enums.Part;
import com.connectcrew.teamone.api.projectservice.leader.ApplyResponse;
import com.connectcrew.teamone.api.projectservice.leader.ApplyStatusResponse;
import com.connectcrew.teamone.api.projectservice.member.ApplyRequest;
import com.connectcrew.teamone.api.projectservice.member.MemberResponse;
import com.connectcrew.teamone.api.userservice.notification.error.ErrorLevel;
import com.connectcrew.teamone.projectservice.global.exceptions.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.QueryMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.SaveMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.UpdateMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.ApplyCommand;
import com.connectcrew.teamone.projectservice.member.application.port.in.query.ProjectApplyQuery;
import com.connectcrew.teamone.projectservice.member.application.port.in.query.ProjectApplyStatusQuery;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.ApplyStatus;
import com.connectcrew.teamone.projectservice.notification.application.port.in.SendNotificationUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.QueryProjectUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final QueryMemberUseCase queryMemberUseCase;
    private final QueryProjectUseCase queryProjectUseCase;

    private final UpdateMemberUseCase updateMemberUseCase;
    private final SaveMemberUseCase saveMemberUseCase;

    private final SendNotificationUseCase sendNotificationUseCase;
    private final SendErrorNotificationUseCase sendErrorNotificationUseCase;

    @GetMapping("/members/{projectId}")
    public Flux<MemberResponse> getProjectMembers(@PathVariable Long projectId) {
        log.trace("getProjectMembers - projectId: {}", projectId);
        return queryProjectUseCase.findLeaderByProject(projectId)
                .doOnNext(leaderId -> log.trace("getProjectMembers - leaderId: {}", leaderId))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("존재하지 않는 프로젝트입니다.")))
                .flatMapMany(leaderId -> queryMemberUseCase.findAllByProject(projectId)
                        .map(member -> member.toResponse(member.user().equals(leaderId)))
                )
                .doOnNext(member -> log.trace("getProjectMembers - member: {}", member))
                .doOnError(ex -> sendErrorNotificationUseCase.send("MemberController.getProjectMembers", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/member/apply")
    @Transactional
    public Mono<Boolean> apply(@RequestBody ApplyRequest request) {
        log.trace("apply - request: {}", request);
        return saveMemberUseCase.apply(ApplyCommand.from(request))
                .flatMap(apply -> sendNotificationUseCase.sendToLeader(
                        request.projectId(),
                        "새로운 지원자가 있습니다!",
                        "지원자를 확인해주세요!",
                        String.format("/apply/project/%d/apply/%d/user/%d", apply.projectId(), apply.id(), apply.userId())
                ))
                .doOnError(ex -> sendErrorNotificationUseCase.send("MemberController.apply", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/applyStatus")
    public Flux<ApplyStatusResponse> getApplyList(Long projectId, Long userId) {
        log.trace("getApplyList - projectId: {}, userId: {}", projectId, userId);
        return queryMemberUseCase.findAllApplyStatus(new ProjectApplyStatusQuery(projectId, userId))
                .map(ApplyStatus::toResponse);
    }

    @GetMapping("/applies")
    public Flux<ApplyResponse> getPartApplyList(Long projectId, Part part, Long userId) {
        log.trace("getPartApplyList - projectId: {}, part: {}, userId: {}", projectId, part, userId);
        return queryMemberUseCase.findAllApplies(new ProjectApplyQuery(projectId, userId, part))
                .map(Apply::toResponse);
    }

    @PostMapping("/apply/{applyId}/leader/{leaderId}/accept")
    @Transactional
    public Mono<ApplyResponse> acceptApply(@PathVariable Long applyId, @PathVariable Long leaderId) {
        log.trace("acceptApply - applyId: {}, leaderId: {}", applyId, leaderId);
        return updateMemberUseCase.accept(applyId, leaderId)
                .map(Apply::toResponse);
    }

    @DeleteMapping("/apply/{applyId}/leader/{leaderId}/reject")
    @Transactional
    public Mono<ApplyResponse> rejectApply(@PathVariable Long applyId, @PathVariable Long leaderId) {
        log.trace("rejectApply - applyId: {}, leaderId: {}", applyId, leaderId);
        return updateMemberUseCase.reject(applyId, leaderId)
                .map(Apply::toResponse);
    }
}
