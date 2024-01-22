package com.connectcrew.teamone.api.projectservice.project;

import com.connectcrew.teamone.api.projectservice.enums.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record ProjectApiResponse(
        Long id,
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        LocalDateTime createdAt,
        ProjectState state,
        Career careerMin,
        Career careerMax,
        UUID chatRoomId,
        List<ProjectCategory> category,
        ProjectGoal goal,
        Long leader,
        List<MemberPart> leaderParts,
        String introduction,
        Integer favorite,
        List<RecruitStatusApiResponse> recruitStatuses,
        List<String> skills
) {
}
