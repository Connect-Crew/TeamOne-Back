package com.connectcrew.teamone.api.project;

import com.connectcrew.teamone.api.project.values.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public record ProjectFilterOption(
        @RequestParam(required = false, defaultValue = "-1") int lastId,
        int size,
        @RequestParam(required = false) ProjectGoal goal,
        @RequestParam(required = false) Career career,
        @RequestParam(required = false) List<Region> region,
        @RequestParam(required = false) Boolean online,
        @RequestParam(required = false) MemberPart part,
        @RequestParam(required = false) List<SkillType> skills,
        @RequestParam(required = false) List<ProjectState> states,
        @RequestParam(required = false) List<ProjectCategory> category
) {
}
