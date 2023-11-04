package com.connectcrew.teamone.compositeservice.resposne;

import com.connectcrew.teamone.api.project.ProjectDetail;
import com.connectcrew.teamone.api.project.values.ProjectCategory;
import com.connectcrew.teamone.api.project.values.SkillType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailRes(
        Long id,
        String title,
        List<String> banners,
        String region,
        Boolean online,
        LocalDateTime createdAt,
        LocalDate startDate,
        LocalDate endDate,
        String state,
        String careerMin,
        String careerMax,
        List<String> category,
        String goal,
        String leader, // TODO 임시 코드 (향후 객체로 수정)
        String introduction,
        Integer favorite,
        List<RecruitStatusRes> recruitStatus,
        List<ProjectMemberRes> members,
        List<String> skills
) {

    public ProjectDetailRes(ProjectDetail detail) {
        this(
                detail.id(),
                detail.title(),
                detail.banners(),
                detail.region().getDescription(),
                detail.online(),
                detail.createdAt(),
                detail.startDate(),
                detail.endDate(),
                detail.state().getDescription(),
                detail.careerMin().getDescription(),
                detail.careerMax().getDescription(),
                detail.category().stream().map(ProjectCategory::getDescription).toList(),
                detail.goal().getDescription(),
                "" + detail.leader(), // TODO 임시 코드 (향후 객체로 수정)
                detail.introduction(),
                detail.favorite(),
                detail.recruitStatuses().stream().map(RecruitStatusRes::new).toList(),
                detail.members().stream().map(ProjectMemberRes::new).toList(),
                detail.skills().stream().map(SkillType::name).toList()
        );
    }
}
