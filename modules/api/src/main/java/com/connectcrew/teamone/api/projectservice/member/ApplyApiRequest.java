package com.connectcrew.teamone.api.projectservice.member;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

public record ApplyApiRequest(
        Long userId,
        Long projectId,
        MemberPart part,
        String message
) {
}
