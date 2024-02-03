package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.projectservice.enums.ApplyState;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyApiResponse;
import com.connectcrew.teamone.api.projectservice.member.ApplyApiRequest;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.ApplyResponse;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.ProfileResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Apply(
        Long id,
        Long userId,
        Long projectId,
        MemberPart part,
        String message,
        String contact,
        ApplyState state,
        String leaderMessage,
        LocalDateTime leaderResponseAt
) {

    public ApplyResponse toResponse(FullProfile profile) {
        return new ApplyResponse(id, projectId, ProfileResponse.from(profile), part, message, contact, state, leaderMessage, leaderResponseAt);
    }

    public static Apply of(ApplyApiResponse res) {
        return new Apply(
                res.id(),
                res.userId(),
                res.projectId(),
                res.part(),
                res.message(),
                res.contact(),
                res.state(),
                res.leaderMessage(),
                res.leaderResponseAt()
        );
    }

    public ApplyApiRequest toApiRequest() {
        return new ApplyApiRequest(
                userId,
                projectId,
                part,
                message,
                contact
        );
    }
}
