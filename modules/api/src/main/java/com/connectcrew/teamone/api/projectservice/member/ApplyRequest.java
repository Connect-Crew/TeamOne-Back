package com.connectcrew.teamone.api.projectservice.member;

import com.connectcrew.teamone.api.projectservice.enums.Part;

public record ApplyRequest(
        Long userId,
        Long projectId,
        Part part,
        String message
) {
}
