package com.connectcrew.teamone.projectservice.project.adapter.in.web.request;

import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.projectservice.project.application.port.in.query.ProjectQuery;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public record ProjectFilterOptionRequest(
        @RequestParam(required = false, defaultValue = "-1") Integer lastId,
        int size,
        @RequestParam(required = false) ProjectGoal goal,
        @RequestParam(required = false) Career career,
        @RequestParam(required = false) List<Region> region,
        @RequestParam(required = false) Boolean online,
        @RequestParam(required = false) MemberPart part,
        @RequestParam(required = false) List<String> skills,
        @RequestParam(required = false) List<ProjectState> states,
        @RequestParam(required = false) List<ProjectCategory> category,
        @RequestParam(required = false, defaultValue = "") String search
) {

    public ProjectQuery toQuery() {
        return ProjectQuery.builder()
                .lastId(lastId)
                .size(size)
                .goal(goal)
                .career(career)
                .region(region)
                .online(online)
                .part(part)
                .skills(skills)
                .states(states)
                .category(category)
                .search(search)
                .build();
    }
}
