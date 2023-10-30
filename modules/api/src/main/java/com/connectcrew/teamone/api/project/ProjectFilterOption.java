package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.Career;
import com.connectcrew.teamone.api.project.values.ProjectGoal;
import com.connectcrew.teamone.api.project.values.Region;

import java.time.LocalDate;
import java.util.List;

public record ProjectFilterOption(
        ProjectGoal goal,
        Career career,
        Region region,
        Boolean online,
        String part,
        LocalDate startDate,
        LocalDate endDate,
        List<String> category
) {
}
