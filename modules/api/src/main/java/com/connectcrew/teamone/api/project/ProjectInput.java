package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.ProjectGoal;
import com.connectcrew.teamone.api.project.values.ProjectState;
import com.connectcrew.teamone.api.project.values.Region;

import java.time.LocalDate;
import java.util.List;

public record ProjectInput(
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        LocalDate start,
        LocalDate end,
        ProjectState state,
        String careerMin,
        String careerMax,
        Long leader,
        List<String> leaderParts,
        List<String> category,
        ProjectGoal goal,
        String introduction,
        List<RecruitInput> recruits,
        String membersIntroduction,
        List<String> skills
) {
}
