package com.connectcrew.teamone.api.projectservice.leader;

import com.connectcrew.teamone.api.projectservice.enums.Part;

public record ApplyResponse(
        Long id,
        Long userId,
        Long projectId,
        Part part,
        String message
) {
}
