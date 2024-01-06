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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final QueryMemberUseCase queryMemberUseCase;
    private final QueryProjectUseCase queryProjectUseCase;

    private final UpdateMemberUseCase updateMemberUseCase;
    private final SaveMemberUseCase saveMemberUseCase;

    private final SendNotificationUseCase sendNotificationUseCase;
    private final SendErrorNotificationUseCase sendErrorNotificationUseCase;

    @GetMapping("/project/project/{projectId}/members")
    public Flux<MemberResponse> getProjectMembers(@PathVariable Long projectId) {
        return queryProjectUseCase.findLeaderByProject(projectId)
                .flatMapMany(leaderId -> queryMemberUseCase.findAllByProject(projectId)
                        .map(member -> member.toResponse(member.user().equals(leaderId))))
                .doOnError(ex -> sendErrorNotificationUseCase.send("MemberController.getProjectMembers", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/apply")
    public Mono<Boolean> apply(@RequestBody ApplyRequest request) {
        return saveMemberUseCase.apply(ApplyCommand.from(request))
                .flatMap(result -> {
                    if (!result) return Mono.just(false);
                    return sendNotificationUseCase.sendToLeader(request.projectId(), "새로운 지원자가 있습니다!", "지원자를 확인해주세요!", "");
                })
                .doOnError(ex -> sendErrorNotificationUseCase.send("MemberController.apply", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/applyStatus")
    Flux<ApplyStatusResponse> getApplyList(Long projectId, Long userId) {
        return queryMemberUseCase.findAllApplyStatus(new ProjectApplyStatusQuery(projectId, userId))
                .map(ApplyStatus::toResponse);
    }

    @GetMapping("/applies")
    Flux<ApplyResponse> getPartApplyList(Long projectId, Part part, Long userId) {
        return queryMemberUseCase.findAllApplies(new ProjectApplyQuery(projectId, userId, part))
                .map(Apply::toResponse);
    }

    @PostMapping("/apply/{applyId}/leader/{leaderId}/accept")
    Mono<ApplyResponse> acceptApply(@PathVariable Long applyId, @PathVariable Long leaderId) {
        return updateMemberUseCase.accept(applyId, leaderId)
                .map(Apply::toResponse);
    }

    @DeleteMapping("/apply/{applyId}/leader/{leaderId}/reject")
    Mono<ApplyResponse> rejectApply(@PathVariable Long applyId, @PathVariable Long leaderId) {
        return updateMemberUseCase.reject(applyId, leaderId)
                .map(Apply::toResponse);
    }
}
