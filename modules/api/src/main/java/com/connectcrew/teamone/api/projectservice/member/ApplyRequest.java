package com.connectcrew.teamone.api.projectservice.member;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

public record ApplyRequest(
        Long userId,
        Long projectId,
        MemberPart part,
        String message
) {
}
