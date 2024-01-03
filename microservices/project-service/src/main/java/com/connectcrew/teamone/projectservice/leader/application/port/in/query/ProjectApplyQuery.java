package com.connectcrew.teamone.projectservice.leader.application.port.in.query;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

public record ProjectApplyQuery(
        Long leader,
        Long projectId,
        MemberPart part
) {
}
