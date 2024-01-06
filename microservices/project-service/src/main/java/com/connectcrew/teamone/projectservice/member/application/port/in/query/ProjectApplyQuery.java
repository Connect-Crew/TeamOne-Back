package com.connectcrew.teamone.projectservice.member.application.port.in.query;

import com.connectcrew.teamone.api.projectservice.enums.Part;

public record ProjectApplyQuery(
        Long leader,
        Long projectId,
        Part part
) {
}
