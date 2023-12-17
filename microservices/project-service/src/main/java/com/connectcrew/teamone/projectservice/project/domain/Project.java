package com.connectcrew.teamone.projectservice.project.domain;

import com.connectcrew.teamone.api.project.values.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record Project(
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
        String introduction,
        Integer favorite,
        List<RecruitStatus> recruitStatuses,
        List<String> skills
) {

}