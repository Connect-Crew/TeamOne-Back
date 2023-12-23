package com.connectcrew.teamone.projectservice.member.adapter.in.web;

import com.connectcrew.teamone.projectservice.global.exceptions.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.projectservice.global.exceptions.enums.ErrorLevel;
import com.connectcrew.teamone.projectservice.member.adapter.in.web.request.ApplyRequest;
import com.connectcrew.teamone.projectservice.member.adapter.in.web.response.MemberResponse;
import com.connectcrew.teamone.projectservice.member.application.port.in.QueryMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.UpdateMemberUseCase;
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

    private final SendErrorNotificationUseCase sendErrorNotificationUseCase;

    @GetMapping("/members")
    public Mono<List<MemberResponse>> getProjectMembers(Long id) {
        return queryMemberUseCase.findAllByProject(id)
                .map(MemberResponse::from)
                .doOnError(ex -> sendErrorNotificationUseCase.send("MemberController.getProjectMembers", ErrorLevel.ERROR, ex));

    }

    @PostMapping("/apply")
    public Mono<Boolean> apply(@RequestBody ApplyRequest request) {
        return updateMemberUseCase.apply(request.toCommand())
                .doOnError(ex -> sendErrorNotificationUseCase.send("MemberController.apply", ErrorLevel.ERROR, ex));
    }
}
