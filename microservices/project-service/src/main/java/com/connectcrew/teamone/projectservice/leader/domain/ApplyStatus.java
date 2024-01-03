package com.connectcrew.teamone.projectservice.leader.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyStatusResponse;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;

public record ApplyStatus(
        MemberPart part,
        int applies,
        int current,
        int max
) {

    public ApplyStatusResponse toResponse() {
        return new ApplyStatusResponse(applies, current, max);
    }

    public static ApplyStatus of(RecruitStatus recruitStatus, int applies) {
        return new ApplyStatus(
                recruitStatus.part(),
                applies,
                recruitStatus.current(),
                recruitStatus.max()
        );
    }
}
