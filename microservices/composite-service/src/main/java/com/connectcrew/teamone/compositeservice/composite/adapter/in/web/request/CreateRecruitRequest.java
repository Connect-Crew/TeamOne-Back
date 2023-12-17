package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateRecruitCommand;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.MemberPart;

public record CreateRecruitRequest(MemberPart part, String comment, Integer max) {
    public CreateRecruitCommand toCommand() {
        return new CreateRecruitCommand(
                part(),
                comment(),
                max()
        );
    }
}
