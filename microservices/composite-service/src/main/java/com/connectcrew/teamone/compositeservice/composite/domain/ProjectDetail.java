package com.connectcrew.teamone.compositeservice.composite.domain;

import com.connectcrew.teamone.compositeservice.composite.domain.enums.Career;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectCategory;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectGoal;
import com.connectcrew.teamone.compositeservice.composite.domain.enums.ProjectState;
import com.connectcrew.teamone.compositeservice.global.enums.Region;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProjectDetail(
        Long id,
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        LocalDateTime createdAt,
        ProjectState state,
        Career careerMin,
        Career careerMax,
        String chatRoomId,
        List<ProjectCategory> category,
        ProjectGoal goal,
        Long leader,
        List<String> leaderParts,
        String introduction,
        Integer favorite,
        List<RecruitStatus> recruitStatuses,
        List<String> skills
) {

}
