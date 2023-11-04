package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public record ProjectFilterOption(
        @RequestParam(required = false, defaultValue = "-1") int lastId,
        int size,
        ProjectGoal goal,
        Career career,
        Region region,
        Boolean online,
        MemberPart part,
        LocalDate startDate,
        LocalDate endDate,
        List<ProjectCategory> category
) {
}
