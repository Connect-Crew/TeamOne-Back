package com.connectcrew.teamone.projectservice.member.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyStatusApiResponse;
import com.connectcrew.teamone.projectservice.project.domain.ProjectPart;

public record ApplyStatus(
        MemberPart part,
        Long applies,
        Long current,
        Long max
) {

    public ApplyStatusApiResponse toResponse() {
        return new ApplyStatusApiResponse(part, applies, current, max);
    }

    public static ApplyStatus of(ProjectPart recruitStatus, Long applies) {
        return new ApplyStatus(
                recruitStatus.part(),
                applies,
                recruitStatus.current(),
                recruitStatus.max()
        );
    }
}
