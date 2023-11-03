package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.MemberPart;

public record RecruitStatus(
        MemberPart part,
        String comment,
        Integer current,
        Integer max
) {
}
