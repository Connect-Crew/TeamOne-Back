package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import com.connectcrew.teamone.api.projectservice.enums.ApplyState;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.compositeservice.composite.domain.Apply;

;

public record ApplyRequest(
        Long projectId,
        MemberPart part,
        String message,
        String contact
) {

    public Apply toDomain(Long userId) {
        return new Apply(userId, projectId, part, message, contact, ApplyState.WAITING, null);
    }
}
