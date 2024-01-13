package com.connectcrew.teamone.api.projectservice.project;

import com.connectcrew.teamone.api.projectservice.enums.MemberPartCategory;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import lombok.Builder;

@Builder
public record RecruitStatusResponse (
        MemberPartCategory category,
        MemberPart part,
        String comment,
        Integer current,
        Integer max,
        boolean applied
){
}
