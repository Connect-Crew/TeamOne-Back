package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import com.connectcrew.teamone.compositeservice.composite.domain.Apply;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;

public record ApplyRequest(
        Long projectId,
        MemberPart part,
        String message
) {

    public Apply toDomain(Long userId) {
        return new Apply(userId, projectId, part, message);
    }
}
