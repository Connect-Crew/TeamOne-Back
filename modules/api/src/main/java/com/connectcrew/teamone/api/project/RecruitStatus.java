package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.MemberPart;
import lombok.Builder;

@Builder
public record RecruitStatus(
        MemberPart part,
        String comment,
        Integer current,
        Integer max
) {
}
