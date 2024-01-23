package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyStatusApiResponse;
import com.connectcrew.teamone.compositeservice.composite.adapter.in.web.response.ApplyStatusResponse;

public record ApplyStatus(
        MemberPart part,
        Long applies,
        Long current,
        Long max,
        String comment
) {

    public ApplyStatusResponse toResponse() {
        return new ApplyStatusResponse(applies, current, max, comment);
    }

    public static ApplyStatus of(ApplyStatusApiResponse res) {
        return new ApplyStatus(
                res.part(),
                res.applies(),
                res.current(),
                res.max(),
                res.comment()
        );
    }
}
