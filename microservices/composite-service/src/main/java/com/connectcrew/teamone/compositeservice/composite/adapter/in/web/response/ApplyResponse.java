package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response;


import com.connectcrew.teamone.api.projectservice.enums.ApplyState;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

public record ApplyResponse(
        Long projectId,
        Long userId,
        MemberPart part,
        String message,
        ApplyState state,
        String leaderMessage
) {
}
