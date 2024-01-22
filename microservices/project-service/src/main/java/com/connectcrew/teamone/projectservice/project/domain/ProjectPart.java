package com.connectcrew.teamone.projectservice.project.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.project.RecruitStatusApiResponse;
import lombok.Builder;

@Builder
public record ProjectPart(
        Long id,
        MemberPart part,
        String comment,
        Long current,
        Long max
) {

    public ProjectPart update(String comment, Long current, Long max) {
        return new ProjectPart(
                id,
                part,
                comment,
                current,
                max
        );
    }

    public RecruitStatusApiResponse toResponse(boolean applied) {
        return RecruitStatusApiResponse.builder()
                .category(part.getCategory())
                .part(part)
                .comment(comment)
                .current(current)
                .max(max)
                .applied(applied)
                .build();
    }
}
