package com.connectcrew.teamone.api.projectservice.project;

import com.connectcrew.teamone.api.projectservice.enums.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public record ProjectFilterOptionRequest(
        @RequestParam(required = false, defaultValue = "-1") Integer lastId,
        int size,
        @RequestParam(required = false) String goal,
        @RequestParam(required = false) Career career,
        @RequestParam(required = false) List<Region> region,
        @RequestParam(required = false) Boolean online,
        @RequestParam(required = false) MemberPart part,
        @RequestParam(required = false) List<String> skills,
        @RequestParam(required = false) List<ProjectState> states,
        @RequestParam(required = false) List<ProjectCategory> category,
        @RequestParam(required = false, defaultValue = "") String search
) {

}
