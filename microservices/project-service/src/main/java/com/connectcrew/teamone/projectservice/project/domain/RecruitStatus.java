package com.connectcrew.teamone.projectservice.project.domain;

import com.connectcrew.teamone.api.project.values.MemberPart;
import lombok.Builder;

@Builder
public record RecruitStatus(
        Long id,
        MemberPart part,
        String comment,
        Integer current,
        Integer max
) {
}
