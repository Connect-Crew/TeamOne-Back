package com.connectcrew.teamone.api.projectservice.project;

import com.connectcrew.teamone.api.projectservice.enums.MemberPartCategory;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import lombok.Builder;

@Builder
public record RecruitStatusApiResponse(
        MemberPartCategory category,
        MemberPart part,
        String comment,
        Long current,
        Long max,
        boolean applied
){
}
