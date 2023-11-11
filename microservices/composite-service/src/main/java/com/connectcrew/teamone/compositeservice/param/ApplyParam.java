package com.connectcrew.teamone.compositeservice.param;

import com.connectcrew.teamone.api.project.ApplyInput;
import com.connectcrew.teamone.api.project.values.MemberPart;

public record ApplyParam(
        Long projectId,
        MemberPart part,
        String message
) {

    public ApplyInput toInput(Long userId) {
        return new ApplyInput(userId, projectId, part, message);
    }
}
