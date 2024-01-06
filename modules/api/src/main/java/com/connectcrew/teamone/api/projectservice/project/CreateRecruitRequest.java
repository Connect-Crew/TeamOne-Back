package com.connectcrew.teamone.api.projectservice.project;

import com.connectcrew.teamone.api.projectservice.enums.Part;

public record CreateRecruitRequest(
        Part part,
        String comment,
        Integer max
) {
}
