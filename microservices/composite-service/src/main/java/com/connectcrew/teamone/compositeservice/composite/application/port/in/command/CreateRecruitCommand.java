package com.connectcrew.teamone.compositeservice.composite.application.port.in.command;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;
import com.connectcrew.teamone.compositeservice.composite.domain.vo.CreateRecruitInfo;

public record CreateRecruitCommand(
        MemberPart part,
        String comment,
        Integer max
) {
    public CreateRecruitInfo toDomain() {
        return new CreateRecruitInfo(
                part,
                comment,
                max
        );
    }
}
