package com.connectcrew.teamone.api.projectservice.leader;

import com.connectcrew.teamone.api.projectservice.enums.ApplyState;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

public record ApplyApiResponse(
        Long id,
        Long userId,
        Long projectId,
        MemberPart part,
        String message,
        String contact,
        ApplyState state,
        String leaderMessage
) {
}
