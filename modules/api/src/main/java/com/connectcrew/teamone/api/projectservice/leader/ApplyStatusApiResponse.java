package com.connectcrew.teamone.api.projectservice.leader;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;

public record ApplyStatusApiResponse(
        MemberPart part,
        Long applies,
        Long current,
        Long max
) {

}
