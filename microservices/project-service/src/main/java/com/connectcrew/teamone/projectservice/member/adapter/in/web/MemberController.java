package com.connectcrew.teamone.projectservice.member.adapter.in.web;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyApiResponse;
import com.connectcrew.teamone.api.projectservice.leader.ApplyStatusApiResponse;
import com.connectcrew.teamone.api.projectservice.member.ApplyApiRequest;
import com.connectcrew.teamone.api.projectservice.member.MemberApiResponse;
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
    public Flux<MemberApiResponse> getProjectMembers(@PathVariable Long projectId) {
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
    public Mono<Boolean> apply(@RequestBody ApplyApiRequest request) {
        log.trace("apply - request: {}", request);
        return saveMemberUseCase.apply(ApplyCommand.from(request))
                .thenReturn(true)
                .doOnError(ex -> sendErrorNotificationUseCase.send("MemberController.apply", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/applyStatus")
    public Flux<ApplyStatusApiResponse> getApplyList(Long projectId, Long userId) {
        log.trace("getApplyList - projectId: {}, userId: {}", projectId, userId);
        return queryMemberUseCase.findAllApplyStatus(new ProjectApplyStatusQuery(projectId, userId))
                .map(ApplyStatus::toResponse);
    }

    @GetMapping("/applies")
    public Flux<ApplyApiResponse> getPartApplyList(Long projectId, MemberPart part, Long userId) {
        log.trace("getPartApplyList - projectId: {}, part: {}, userId: {}", projectId, part, userId);
        return queryMemberUseCase.findAllApplies(new ProjectApplyQuery(userId, projectId, part))
                .map(Apply::toResponse);
    }

    @PostMapping("/apply/{applyId}/leader/{leaderId}/accept")
    @Transactional
    public Mono<ApplyApiResponse> acceptApply(@PathVariable Long applyId, @PathVariable Long leaderId, @RequestBody String leaderMessage) {
        log.trace("acceptApply - applyId: {}, leaderId: {}", applyId, leaderId);
        return updateMemberUseCase.accept(applyId, leaderId, leaderMessage)
                .map(Apply::toResponse);
    }

    @PostMapping("/apply/{applyId}/leader/{leaderId}/reject")
    @Transactional
    public Mono<ApplyApiResponse> rejectApply(@PathVariable Long applyId, @PathVariable Long leaderId, @RequestBody String leaderMessage) {
        log.trace("rejectApply - applyId: {}, leaderId: {}", applyId, leaderId);
        return updateMemberUseCase.reject(applyId, leaderId, leaderMessage)
                .map(Apply::toResponse);
    }
}
