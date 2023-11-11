package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.MemberPart;

public record ApplyInput(
        Long userId,
        Long projectId,
        MemberPart part,
        String message
) {
}
