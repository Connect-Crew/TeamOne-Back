package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.MemberPart;

public record RecruitInput(
        MemberPart part,
        String comment,
        Integer max
) {
}
