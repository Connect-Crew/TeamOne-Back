package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.api.projectservice.project.CreateProjectRequest;
import com.connectcrew.teamone.projectservice.project.domain.Banner;
import com.connectcrew.teamone.projectservice.project.domain.Category;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.Skill;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.connectcrew.teamone.projectservice.global.constants.PatternConstanst.UUID_PATTERNS;

public record SaveProjectCommand(
        String title,
        List<String> banners,
        Region region,
        Boolean online,
        ProjectState state,
        UUID chatRoomId,
        Career careerMin,
        Career careerMax,
        Long leader,
        List<Part> leaderParts,
        List<ProjectCategory> category,
        ProjectGoal goal,
        String introduction,
        List<CreateRecruitCommand> recruits,
        List<String> skills
) {
    public static SaveProjectCommand from(CreateProjectRequest request) {
        return new SaveProjectCommand(
                request.title(),
                request.banners(),
                request.region(),
                request.online(),
                request.state(),
                request.chatRoomId(),
                request.careerMin(),
                request.careerMax(),
                request.leader(),
                request.leaderParts(),
                request.category(),
                request.goal(),
                request.introduction(),
                request.recruits().stream().map(CreateRecruitCommand::from).toList(),
                request.skills()
        );
    }

    public Project toDomain() {
        return Project.builder()
                .title(title)
                .banners(banners.stream().map(b -> new Banner(null, b)).toList())
                .region(region)
                .online(online)
                .state(state)
                .chatRoomId(chatRoomId)
                .careerMin(careerMin)
                .careerMax(careerMax)
                .leader(leader)
                .category(category.stream().map(c -> new Category(null, c)).toList())
                .goal(goal)
                .introduction(introduction)
                .parts(recruits.stream().map(r -> r.toDomain(leaderParts.contains(r.part()))).toList())
                .favorite(0)
                .skills(skills.stream().map(s -> new Skill(null, s)).toList())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Mono<Void> validate() {
        // 1. title은 2글자 이상 30글자 이하
        if (title().length() < 2)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.TITLE_LENGTH_2_OVER.toString()));
        if (title().length() > 30)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.TITLE_LENGTH_30_UNDER.toString()));

        // 2. banner는 최대 3개. 경로, 이름, 확장자가 유효한지 검사
        if (banners() != null && banners().size() > 3)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.BANNER_MAX_3.toString()));

        if (banners() != null) {
            for (String banner : banners()) {
                String[] fileNameAndExtensions = banner.split("\\.");
                String filename = fileNameAndExtensions[0];
                String extension = fileNameAndExtensions[1];
                if (!Pattern.matches(UUID_PATTERNS, filename)) {
                    return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_NAME.toString()));
                }

                if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("jpeg")) {
                    return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_EXTENSION.toString()));
                }
            }
        }

        // 4. careerMin은 careerMax보다 이전 값이어야 함.
        if (careerMin().getId() > careerMax().getId())
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.CAREER_MIN_BEFORE_MAX.toString()));

        // 5. chatRoomId는 UUID
//        if (!Pattern.matches(UUID_PATTERNS, chatRoomId()))
//            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_CHATROOM_ID.toString()));

        // 6. category는 최소 1개 최대 3개
        if (category().isEmpty())
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.CATEGORY_MIN_1.toString()));

        if (category().size() > 3)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.CATEGORY_MAX_3.toString()));

        // 7. introduction은 1000글자 이하
        if (introduction().length() > 1000)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.INTRODUCTION_LENGTH_1000_UNDER.toString()));

        // 8. recruit 조건 검사 (comment는 최대 30글자, max는 0 이상인지, 모든 recruit의 max의 합이 10 이하인지)
        int recruitMaxSum = 0;
        for (CreateRecruitCommand recruit : recruits()) {
            if (recruit.comment().length() > 30)
                return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.RECRUIT_COMMENT_LENGTH_30_UNDER.toString()));

            if (recruit.max() < 0)
                return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.RECRUIT_MAX_0_OVER.toString()));

            recruitMaxSum += recruit.max();
        }
        if (recruitMaxSum > 30)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.RECRUIT_MAX_SUM_30_UNDER.toString()));

        return Mono.just(this).then();
    }
}
