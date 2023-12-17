package com.connectcrew.teamone.projectservice.project.adapter.in.web.response;

import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record RecruitStatusResponse (
        MemberPart part,
        String comment,
        Integer current,
        Integer max,
        boolean applied
){

    public static RecruitStatusResponse from(RecruitStatus recruit, boolean applied) {
        return RecruitStatusResponse.builder()
                .part(recruit.part())
                .comment(recruit.comment())
                .current(recruit.current())
                .max(recruit.max())
                .applied(applied)
                .build();
    }

    public static List<RecruitStatusResponse> from(List<RecruitStatus> recruit, Set<MemberPart> applies) {
        return recruit.stream()
                .map(r -> RecruitStatusResponse.from(r, applies.contains(r.part())))
                .toList();
    }
}
