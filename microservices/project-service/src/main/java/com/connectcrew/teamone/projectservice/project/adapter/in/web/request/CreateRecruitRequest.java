package com.connectcrew.teamone.projectservice.project.adapter.in.web.request;


import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;

public record CreateRecruitRequest(
        MemberPart part,
        String comment,
        Integer max
) {
    public RecruitStatus toDomain(boolean containLeader) {
        return RecruitStatus.builder()
                .part(part)
                .comment(comment)
                .current(containLeader ? 1 : 0)
                .max(max)
                .build();
    }
}
