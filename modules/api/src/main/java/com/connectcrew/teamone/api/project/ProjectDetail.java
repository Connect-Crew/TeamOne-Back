package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.Career;
import com.connectcrew.teamone.api.project.values.ProjectGoal;
import com.connectcrew.teamone.api.project.values.ProjectState;
import com.connectcrew.teamone.api.project.values.Region;
import lombok.Builder;

import java.time.LocalDate;
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
        LocalDate startDate,
        LocalDate endDate,
        ProjectState state,
        Career careerMin,
        Career careerMax,
        List<String> category,
        ProjectGoal goal,
        Long leader,
        String introduction,
        Integer favorite,
        List<RecruitStatus> recruitStatuses,
        List<ProjectMember> members,
        List<String> skills
) {

}
