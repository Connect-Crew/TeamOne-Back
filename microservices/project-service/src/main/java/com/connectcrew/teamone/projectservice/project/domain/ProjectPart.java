package com.connectcrew.teamone.projectservice.project.domain;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.project.RecruitStatusResponse;
import lombok.Builder;

@Builder
public record ProjectPart(
        Long id,
        MemberPart part,
        String comment,
        Integer current,
        Integer max
) {

    public ProjectPart update(String comment, Integer current, Integer max) {
        return new ProjectPart(
                id,
                part,
                comment,
                current,
                max
        );
    }

    public RecruitStatusResponse toResponse(boolean applied) {
        return RecruitStatusResponse.builder()
                .category(part.getCategory())
                .part(part)
                .comment(comment)
                .current(current)
                .max(max)
                .applied(applied)
                .build();
    }
//
//    public static ProjectPart from(CreateRecruitCommand request, boolean containLeader) {
//        return ProjectPart.builder()
//                .part(request.part())
//                .comment(request.comment())
//                .current(containLeader ? 1 : 0)
//                .max(request.max())
//                .build();
//    }
//
//    public static ProjectPart from(CreateRecruitCommand request, int current) {
//        return ProjectPart.builder()
//                .part(request.part())
//                .comment(request.comment())
//                .current(current)
//                .max(request.max())
//                .build();
//    }
}
