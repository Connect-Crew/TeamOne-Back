package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;

public record Apply(
        Long userId,
        Long projectId,
        MemberPart part,
        String message
) {
}
