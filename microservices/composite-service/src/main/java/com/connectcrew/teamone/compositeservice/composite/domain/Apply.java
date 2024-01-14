package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyApiResponse;
import com.connectcrew.teamone.api.projectservice.member.ApplyApiRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.ApplyResponse;

public record Apply(
        Long userId,
        Long projectId,
        MemberPart part,
        String message
) {

    public ApplyResponse toResponse() {
        return new ApplyResponse(projectId, userId, part, message);
    }

    public static Apply of(ApplyApiResponse res) {
        return new Apply(
                res.userId(),
                res.projectId(),
                res.part(),
                res.message()
        );
    }

    public ApplyApiRequest toApiRequest() {
        return new ApplyApiRequest(
                userId,
                projectId,
                part,
                message
        );
    }
}
