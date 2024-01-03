package com.connectcrew.teamone.api.projectservice.leader;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

public record ApplyResponse(
        Long id,
        Long userId,
        Long projectId,
        MemberPart part,
        String message
) {
}
