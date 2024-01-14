package com.connectcrew.teamone.projectservice.project.application.port.in.command;

import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.api.projectservice.enums.*;
import com.connectcrew.teamone.api.projectservice.project.UpdateProjectApiRequest;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.project.domain.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.connectcrew.teamone.projectservice.global.constants.PatternConstanst.UUID_PATTERNS;

public record UpdateProjectCommand(
        Long projectId,
        Long userId,
        String title,
        List<String> banners,
        List<String> removeBanners,
        Region region,
        Boolean online,
        ProjectState state,
        Career careerMin,
        Career careerMax,
        List<MemberPart> leaderParts,
        List<ProjectCategory> category,
        ProjectGoal goal,
        String introduction,
        List<CreateRecruitCommand> recruits,
        List<String> skills
) {

    public static UpdateProjectCommand from(UpdateProjectApiRequest request) {
        Map<MemberPart, CreateRecruitCommand> recruits = request.recruits().stream()
                .map(CreateRecruitCommand::from)
                .collect(Collectors.toMap(CreateRecruitCommand::part, r -> r));

        for (MemberPart part : request.leaderParts()) {
            if (!recruits.containsKey(part)) {
                recruits.put(part, new CreateRecruitCommand(part, "리더의 직무입니다.", 1L));
            } else {
                CreateRecruitCommand recruit = recruits.get(part);
                recruits.put(part, new CreateRecruitCommand(part, recruit.comment(), recruit.max() + 1));
            }
        }

        return new UpdateProjectCommand(
                request.projectId(),
                request.userId(),
                request.title(),
                request.banners(),
                request.removeBanners(),
                request.region(),
                request.online(),
                request.state(),
                request.careerMin(),
                request.careerMax(),
                request.leaderParts(),
                request.category(),
                request.goal(),
                request.introduction(),
                recruits.values().stream().toList(),
                request.skills()
        );
    }

    public Project toDomain(Project origin, Member originLeader) {

        Map<String, Long> bannerIdMap = origin.banners().stream()
                .filter(b -> !removeBanners.contains(b.path()))
                .collect(Collectors.toMap(Banner::path, Banner::id));

        List<Banner> banners = new ArrayList<>();
        bannerIdMap.forEach((k, v) -> banners.add(new Banner(v, k)));
        this.banners.forEach(b -> new Banner(bannerIdMap.getOrDefault(b, null), b));

        Map<ProjectCategory, Long> categoryIdMap = origin.category().stream()
                .collect(Collectors.toMap(Category::category, Category::id));

        Map<String, Long> skillIdMap = origin.skills().stream()
                .collect(Collectors.toMap(Skill::skill, Skill::id));

        Map<MemberPart, ProjectPart> partProjectPartMap = origin.parts().stream()
                .collect(Collectors.toMap(ProjectPart::part, p -> p));

        List<ProjectPart> updatedParts = recruits.stream().map(r -> {
                    if (partProjectPartMap.containsKey(r.part())) {
                        ProjectPart projectPart = partProjectPartMap.get(r.part());

                        long current = projectPart.current();
                        if (originLeader.containPart(r.part()) && !leaderParts.contains(r.part())) {
                            current--; // 기존에는 담당 파트였지만, 이번에는 담당 파트가 아닌 경우
                        } else if (!originLeader.containPart(r.part()) && leaderParts.contains(r.part())) {
                            current++; // 기존에는 담당 파트가 아니었지만, 이번에는 담당 파트인 경우
                        }

                        return projectPart.update(r.comment(), current, r.max());
                    } else {
                        return r.toDomain(leaderParts.contains(r.part()));
                    }
                })
                .toList();

        return Project.builder()
                .id(origin.id())
                .title(title)
                .banners(banners)
                .region(region)
                .online(online)
                .state(state)
                .chatRoomId(origin.chatRoomId())
                .careerMin(careerMin)
                .careerMax(careerMax)
                .leader(origin.leader())
                .category(category.stream().map(c -> new Category(categoryIdMap.getOrDefault(c, null), c)).toList())
                .goal(goal)
                .introduction(introduction)
                .parts(updatedParts)
                .favorite(origin.favorite())
                .skills(skills.stream().map(s -> new Skill(skillIdMap.getOrDefault(s, null), s)).toList())
                .createdAt(origin.createdAt())
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
