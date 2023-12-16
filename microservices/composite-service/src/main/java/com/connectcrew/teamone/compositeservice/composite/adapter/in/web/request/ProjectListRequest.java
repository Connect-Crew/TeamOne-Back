package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.query.FindProjectListQuery;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public record ProjectListRequest(
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
    public FindProjectListQuery toQuery() {
        return new FindProjectListQuery(
                lastId,
                size,
                goal,
                career,
                region,
                online,
                part,
                skills,
                states,
                category,
                search
        );
    }
}
