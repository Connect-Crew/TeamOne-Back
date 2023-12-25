package com.connectcrew.teamone.api.projectservice.project;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

public record CreateRecruitRequest(
        MemberPart part,
        String comment,
        Integer max
) {
}
