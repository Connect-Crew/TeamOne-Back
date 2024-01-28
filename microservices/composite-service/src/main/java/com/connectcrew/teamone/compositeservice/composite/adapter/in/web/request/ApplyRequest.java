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
        return Apply.builder()
                .userId(userId)
                .projectId(projectId)
                .part(part)
                .message(message)
                .contact(contact)
                .state(ApplyState.WAITING)
                .build();
    }
}
