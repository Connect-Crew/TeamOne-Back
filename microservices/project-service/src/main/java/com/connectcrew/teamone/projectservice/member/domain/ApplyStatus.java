package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyStatusApiResponse;
import com.connectcrew.teamone.projectservice.project.domain.ProjectPart;

public record ApplyStatus(
        MemberPart part,
        Long applies,
        Long current,
        Long max,
        String comment
) {

    public ApplyStatusApiResponse toResponse() {
        return new ApplyStatusApiResponse(part, applies, current, max, comment);
    }

    public static ApplyStatus of(ProjectPart projectPart, Long applies) {
        return new ApplyStatus(
                projectPart.part(),
                applies,
                projectPart.current(),
                projectPart.max(),
                projectPart.comment()
        );
    }
}
