package com.connectcrew.teamone.api.projectservice.project;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

public record CreateRecruitApiRequest(
        MemberPart part,
        String comment,
        Long max
) {
}
