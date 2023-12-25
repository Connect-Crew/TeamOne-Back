package com.connectcrew.teamone.projectservice.member.adapter.in.web;

import com.connectcrew.teamone.api.projectservice.member.ApplyRequest;
import com.connectcrew.teamone.api.projectservice.member.MemberResponse;
import com.connectcrew.teamone.api.userservice.notification.error.ErrorLevel;
import com.connectcrew.teamone.projectservice.global.exceptions.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.QueryMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.UpdateMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.ApplyCommand;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.notification.application.port.in.SendNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final QueryMemberUseCase queryMemberUseCase;

    private final UpdateMemberUseCase updateMemberUseCase;

    private final SendNotificationUseCase sendNotificationUseCase;
    private final SendErrorNotificationUseCase sendErrorNotificationUseCase;

    @GetMapping("/members")
    public Mono<List<MemberResponse>> getProjectMembers(Long id) {
        return queryMemberUseCase.findAllByProject(id)
                .map(members -> members.stream().map(Member::toResponse).toList())
                .doOnError(ex -> sendErrorNotificationUseCase.send("MemberController.getProjectMembers", ErrorLevel.ERROR, ex));

    }

    @PostMapping("/apply")
    public Mono<Boolean> apply(@RequestBody ApplyRequest request) {
        return updateMemberUseCase.apply(ApplyCommand.from(request))
                .flatMap(result -> {
                    if (!result) return Mono.just(false);
                    return sendNotificationUseCase.sendToLeader(request.projectId(), "새로운 지원자가 있습니다!", "지원자를 확인해주세요!", "");
                })
                .doOnError(ex -> sendErrorNotificationUseCase.send("MemberController.apply", ErrorLevel.ERROR, ex));
    }
}
