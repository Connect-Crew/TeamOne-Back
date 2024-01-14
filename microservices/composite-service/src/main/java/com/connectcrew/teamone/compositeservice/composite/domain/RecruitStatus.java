package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.project.RecruitStatusApiResponse;
import lombok.Builder;

@Builder
public record RecruitStatus(
        MemberPart part,
        String comment,
        Long current,
        Long max,
        Boolean applied
) {
    public static RecruitStatus of(RecruitStatusApiResponse res) {
        return RecruitStatus.builder()
                .part(res.part())
                .comment(res.comment())
                .current(res.current())
                .max(res.max())
                .applied(res.applied())
                .build();
    }
}
