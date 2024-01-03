package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;

public record ApplyResponse(
        Long projectId,
        Long userId,
        MemberPart part,
        String message
) {
}
