package com.connectcrew.teamone.api.projectservice.project;

import com.connectcrew.teamone.api.projectservice.enums.Part;
import lombok.Builder;

@Builder
public record RecruitStatusResponse (
        Part part,
        String comment,
        Integer current,
        Integer max,
        boolean applied
){
}
