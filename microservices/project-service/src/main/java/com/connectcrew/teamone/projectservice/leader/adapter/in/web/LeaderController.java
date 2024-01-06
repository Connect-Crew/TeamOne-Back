package com.connectcrew.teamone.projectservice.leader.adapter.in.web;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyResponse;
import com.connectcrew.teamone.api.projectservice.leader.ApplyStatusResponse;
import com.connectcrew.teamone.projectservice.leader.application.port.in.QueryApplyUseCase;
import com.connectcrew.teamone.projectservice.leader.application.port.in.UpdateApplyUseCase;
import com.connectcrew.teamone.projectservice.leader.application.port.in.query.ProjectApplyQuery;
import com.connectcrew.teamone.projectservice.leader.application.port.in.query.ProjectApplyStatusQuery;
import com.connectcrew.teamone.projectservice.leader.domain.ApplyStatus;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/leader")
@RequiredArgsConstructor
class LeaderController {
    private final QueryApplyUseCase queryApplyUseCase;
    private final UpdateApplyUseCase updateApplyUseCase;

    @GetMapping("/applyStatus")
    Flux<ApplyStatusResponse> getApplyList(Long projectId, Long userId) {
        return queryApplyUseCase.findAllApplyStatus(new ProjectApplyStatusQuery(projectId, userId))
                .map(ApplyStatus::toResponse);
    }

    @GetMapping("/applies")
    Flux<ApplyResponse> getPartApplyList(Long projectId, MemberPart part, Long userId) {
        return queryApplyUseCase.findAllApplies(new ProjectApplyQuery(projectId, userId, part))
                .map(Apply::toResponse);
    }

    @PostMapping("/apply/{applyId}/leader/{leaderId}/accept")
    Mono<ApplyResponse> acceptApply(@PathVariable Long applyId, @PathVariable Long leaderId) {
        return updateApplyUseCase.accept(applyId, leaderId)
                .map(Apply::toResponse);
    }

    @DeleteMapping("/apply/{applyId}/leader/{leaderId}/reject")
    Mono<ApplyResponse> rejectApply(@PathVariable Long applyId, @PathVariable Long leaderId) {
        return updateApplyUseCase.reject(applyId, leaderId)
                .map(Apply::toResponse);
    }
}