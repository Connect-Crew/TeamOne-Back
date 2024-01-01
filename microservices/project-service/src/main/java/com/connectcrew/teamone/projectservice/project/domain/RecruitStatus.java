package com.connectcrew.teamone.projectservice.project.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.project.RecruitStatusResponse;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.CreateRecruitCommand;
import lombok.Builder;

@Builder
public record RecruitStatus(
        Long id,
        MemberPart part,
        String comment,
        Integer current,
        Integer max
) {

    public RecruitStatusResponse toResponse(boolean applied) {
        return RecruitStatusResponse.builder()
                .part(part)
                .comment(comment)
                .current(current)
                .max(max)
                .applied(applied)
                .build();
    }

    public static RecruitStatus from(CreateRecruitCommand request, boolean containLeader) {
        return RecruitStatus.builder()
                .part(request.part())
                .comment(request.comment())
                .current(containLeader ? 1 : 0)
                .max(request.max())
                .build();
    }

    public static RecruitStatus from(CreateRecruitCommand request, int current) {
        return RecruitStatus.builder()
                .part(request.part())
                .comment(request.comment())
                .current(current)
                .max(request.max())
                .build();
    }
}
