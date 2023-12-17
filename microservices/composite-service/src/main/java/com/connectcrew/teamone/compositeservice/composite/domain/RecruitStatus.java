package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;
import lombok.Builder;

@Builder
public record RecruitStatus(
        MemberPart part,
        String comment,
        Integer current,
        Integer max,
        Boolean applied
) {
}
