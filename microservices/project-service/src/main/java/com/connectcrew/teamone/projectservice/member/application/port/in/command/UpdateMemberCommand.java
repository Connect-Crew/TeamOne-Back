package com.connectcrew.teamone.projectservice.member.application.port.in.command;

import com.connectcrew.teamone.api.projectservice.enums.Part;

import java.util.List;

public record UpdateMemberCommand(
        Long projectId,
        Long userId,
        List<Part> parts
) {
}
