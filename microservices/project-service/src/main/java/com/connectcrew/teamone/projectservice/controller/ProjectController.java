package com.connectcrew.teamone.projectservice.controller;

import com.connectcrew.teamone.api.exception.InvalidOwnerException;
import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.api.project.*;
import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.projectservice.entity.*;
import com.connectcrew.teamone.projectservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectController {
    private static final String UUID_PATTERNS = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";

    private final ProjectRepository projectRepository;
    private final BannerRepository bannerRepository;
    private final PartRepository partRepository;
    private final ApplyRepository applyRepository;
    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final CustomRepository customRepository;

    private ProjectCustomFindOption paramToOption(ProjectFilterOption option) {
        return new ProjectCustomFindOption(
                option.lastId(),
                option.size(),
                option.goal(),
                option.career(),
                option.region(),
                option.online(),
                option.part(),
                option.skills(),
                option.states(),
                option.category(),
                option.search()
        );
    }

    @GetMapping("/list")
    public Flux<ProjectItem> getProjectList(ProjectFilterOption option) {
        log.trace("getProjectList - option: {}", option);
        return customRepository.findAllByOption(paramToOption(option))
                .flatMap(p -> bannerRepository.findByProjectOrderByIdx(p.id())
                        .defaultIfEmpty(new Banner())
                        .map(b -> Tuples.of(p, b)))
                .flatMap(tuple2 -> partRepository.findAllByProject(tuple2.getT1().id())
                        .collectList()
                        .defaultIfEmpty(Collections.emptyList())
                        .map(parts -> Tuples.of(tuple2.getT1(), tuple2.getT2(), parts)))
                .map(tuple3 -> {
                    ProjectCustomEntity project = tuple3.getT1();
                    Banner banner = tuple3.getT2();
                    List<RecruitStatus> recruitStatuses = tuple3.getT3().stream()
                            .map(p -> RecruitStatus.builder()
                                    .part(MemberPart.valueOf(p.getPart()))
                                    .comment(p.getComment())
                                    .current(p.getCollected())
                                    .max(p.getTargetCollect())
                                    .build())
                            .toList();

                    return ProjectItem.builder()
                            .id(project.id())
                            .title(project.title())
                            .thumbnail(banner.getPath())
                            .region(project.region())
                            .online(project.online())
                            .careerMin(project.careerMin())
                            .careerMax(project.careerMax())
                            .createdAt(project.createdAt())
                            .state(project.state())
                            .favorite(project.favorite())
                            .category(project.category())
                            .goal(project.goal())
                            .recruitStatus(recruitStatuses)
                            .build();
                });
    }

    @PostMapping("/")
    @Transactional
    public Mono<Long> createProject(@RequestBody ProjectInput input) {
        log.trace("createProject - input: {}", input);
        return validateInput(input)
                .then(projectRepository.save(inputToProject(input)))
                .flatMap(p -> saveBanners(input, p.getId()).thenReturn(p))
                .flatMap(p -> saveParts(input, p.getId())
                        .flatMap(parts -> saveLeaderMember(input, p.getId(), parts))
                        .thenReturn(p))
                .flatMap(p -> saveSkills(input, p.getId()).thenReturn(p))
                .flatMap(p -> saveCategories(input, p.getId()).thenReturn(p))
                .map(Project::getId)
                .onErrorResume(e -> {
                    log.error("createProject - error: {}", e.getMessage(), e);
                    return Mono.error(new RuntimeException(ProjectExceptionMessage.CREATE_PROJECT_FAILED.toString()));
                });

    }

    private Mono<ProjectInput> validateInput(ProjectInput input) {
        // 1. title은 2글자 이상 30글자 이하
        if (input.title().length() < 2)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.TITLE_LENGTH_2_OVER.toString()));
        if (input.title().length() > 30)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.TITLE_LENGTH_30_UNDER.toString()));

        // 2. banner는 최대 3개. 경로, 이름, 확장자가 유효한지 검사
        if (input.banners() != null && input.banners().size() > 3)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.BANNER_MAX_3.toString()));

        if (input.banners() != null) {
            for (String banner : input.banners()) {
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
        if (input.careerMin().getId() > input.careerMax().getId())
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.CAREER_MIN_BEFORE_MAX.toString()));

        // 6. category는 최소 1개 최대 3개
        if (input.category().size() < 1)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.CATEGORY_MIN_1.toString()));

        if (input.category().size() > 3)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.CATEGORY_MAX_3.toString()));

        // 7. introduction은 1000글자 이하
        if (input.introduction().length() > 1000)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.INTRODUCTION_LENGTH_1000_UNDER.toString()));

        // 8. recruit 조건 검사 (comment는 최대 30글자, max는 0 이상인지, 모든 recruit의 max의 합이 10 이하인지)
        int recruitMaxSum = 0;
        for (RecruitInput recruit : input.recruits()) {
            if (recruit.comment().length() > 30)
                return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.RECRUIT_COMMENT_LENGTH_30_UNDER.toString()));

            if (recruit.max() < 0)
                return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.RECRUIT_MAX_0_OVER.toString()));

            recruitMaxSum += recruit.max();
        }
        if (recruitMaxSum > 10)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.RECRUIT_MAX_SUM_10_UNDER.toString()));

        return Mono.just(input);
    }

    private Project inputToProject(ProjectInput input) {
        return Project.builder()
                .title(input.title())
                .introduction(input.introduction())
                .careerMin(input.careerMin().getId())
                .careerMax(input.careerMax().getId())
                .leader(input.leader())
                .withOnline(input.online())
                .region(input.region().name())
                .createdAt(LocalDateTime.now())
                .state(input.state().name())
                .goal(input.goal().name())
                .favorite(0)
                .build();
    }

    @NotNull
    private Mono<Long> saveCategories(ProjectInput input, Long id) {
        List<Category> categories = new ArrayList<>();
        for (ProjectCategory category : input.category()) {
            categories.add(Category.builder()
                    .project(id)
                    .name(category.name())
                    .build());
        }

        return categoryRepository.saveAll(categories)
                .then()
                .thenReturn(id);
    }

    @NotNull
    private Mono<Long> saveSkills(ProjectInput input, Long id) {
        List<Skill> skills = new ArrayList<>();
        for (String skill : input.skills()) {
            skills.add(Skill.builder()
                    .project(id)
                    .name(skill)
                    .build());
        }

        return skillRepository.saveAll(skills)
                .then()
                .thenReturn(id);
    }

    @NotNull
    private Mono<List<Part>> saveParts(ProjectInput input, Long id) {
        List<Part> parts = new ArrayList<>();
        for (RecruitInput recruit : input.recruits()) {
            parts.add(Part.builder()
                    .project(id)
                    .partCategory(recruit.part().getCategory().name())
                    .part(recruit.part().name())
                    .comment(recruit.comment())
                    .collected(0)
                    .targetCollect(recruit.max())
                    .build());
        }
        return partRepository.saveAll(parts)
                .collectList();
    }

    @NotNull
    private Mono<Long> saveLeaderMember(ProjectInput input, Long id, List<Part> parts) {
        List<String> leaderParts = input.leaderParts().stream().map(MemberPart::name).toList();
        List<Member> members = new ArrayList<>();
        for (Part part : parts) {
            if (!leaderParts.contains(part.getPart())) continue;

            Member member = Member.builder()
                    .partId(part.getId())
                    .user(input.leader())
                    .build();
            members.add(member);
        }

        return memberRepository.saveAll(members)
                .then()
                .thenReturn(id);
    }

    @NotNull
    private Mono<Long> saveBanners(ProjectInput input, Long id) {
        List<Banner> banners = new ArrayList<>();
        for (int i = 0; i < input.banners().size(); i++) {
            banners.add(Banner.builder()
                    .project(id)
                    .idx(i)
                    .path(input.banners().get(i))
                    .build());
        }

        return bannerRepository.saveAll(banners)
                .then()
                .thenReturn(id);
    }

    @GetMapping("/")
    public Mono<ProjectDetail> getProject(Long id) {
        log.trace("getProject - id: {}", id);
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString())))
                .flatMap(project -> {
                    Mono<List<Banner>> banners = bannerRepository.findAllByProject(id).collectList();
                    Mono<List<Part>> parts = partRepository.findAllByProject(id).collectList();
                    Mono<List<Skill>> skills = skillRepository.findAllByProject(id).collectList();
                    Mono<List<Category>> categories = categoryRepository.findAllByProject(id).collectList();
                    Mono<List<Member>> members = memberRepository.findAllByProject(id).collectList();

                    return Mono.zip(banners, parts, skills, categories, members)
                            .map(tuple -> {
                                List<String> bannerPaths = tuple.getT1().stream()
                                        .sorted(Comparator.comparingInt(Banner::getIdx))
                                        .map(Banner::getPath).toList();

                                List<RecruitStatus> recruits = new ArrayList<>();
                                Map<Long, MemberPart> partMap = new HashMap<>();
                                for (Part p : tuple.getT2()) {
                                    MemberPart part = MemberPart.valueOf(p.getPart());
                                    partMap.put(p.getId(), part);
                                    recruits.add(new RecruitStatus(part, p.getComment(), p.getCollected(), p.getTargetCollect()));
                                }

                                List<String> skillNames = tuple.getT3().stream().map(Skill::getName).toList();

                                List<ProjectCategory> categoryNames = tuple.getT4().stream()
                                        .map(Category::getName).map(ProjectCategory::valueOf).toList();

                                List<ProjectMember> projectMembers = tuple.getT5().stream()
                                        .collect(Collectors.groupingBy(Member::getUser, Collectors.mapping(m -> partMap.get(m.getPartId()), Collectors.toList())))
                                        .entrySet()
                                        .stream().map(e -> new ProjectMember(e.getKey(), e.getValue()))
                                        .toList();


                                return ProjectDetail.builder()
                                        .id(project.getId())
                                        .title(project.getTitle())
                                        .banners(bannerPaths)
                                        .region(Region.valueOf(project.getRegion()))
                                        .online(project.getWithOnline())
                                        .createdAt(project.getCreatedAt())
                                        .state(ProjectState.valueOf(project.getState()))
                                        .careerMin(Career.valueOf(project.getCareerMin()))
                                        .careerMax(Career.valueOf(project.getCareerMax()))
                                        .category(categoryNames)
                                        .goal(ProjectGoal.valueOf(project.getGoal()))
                                        .leader(project.getLeader())
                                        .introduction(project.getIntroduction())
                                        .favorite(project.getFavorite())
                                        .recruitStatuses(recruits)
                                        .members(projectMembers)
                                        .skills(skillNames)
                                        .build();
                            })
                            .onErrorResume(e -> {
                                log.trace("getProject - error: {}", e.getMessage());
                                return Mono.error(new RuntimeException(ProjectExceptionMessage.LOAD_PROJECT_FAILED.toString()));
                            });
                });
    }

    @DeleteMapping("/")
    public Mono<Long> deleteProject(Long id, Long leader) {
        log.trace("deleteProject - id: {}, leader: {}", id, leader);
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString())))
                .flatMap(project -> {
                    if (!project.getLeader().equals(leader))
                        return Mono.error(new InvalidOwnerException(ProjectExceptionMessage.INVALID_PROJECT_OWNER.toString()));

                    return projectRepository.delete(project).thenReturn(project.getId());
                });
    }

    @PostMapping("/apply")
    public Mono<Boolean> applyProject(@RequestBody ApplyInput input) {
        return projectRepository.existsById(input.projectId())
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString()));

                    return partRepository.findByProjectAndPart(input.projectId(), input.part().name());
                })
                .switchIfEmpty(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PART.toString())))
                .flatMap(part -> {
                    if (part.getCollected() >= part.getTargetCollect())
                        return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.COLLECTED_PART.toString()));
                    return Mono.just(part);
                })
                .flatMap(part -> checkAlreadyPartMember(part, input.userId()))
                .flatMap(part -> checkAlreadyApply(part, input.userId()))
                .flatMap(part -> applyRepository.save(Apply.builder()
                        .project(part.getProject())
                        .partId(part.getId())
                        .user(input.userId())
                        .message(input.message())
                        .build())
                )
                .map(apply -> true);
    }

    /**
     * 이미 해당 part의 member인지 확인
     */
    private Mono<Part> checkAlreadyPartMember(Part part, Long userId) {
        return memberRepository.existsByPartIdAndUser(part.getId(), userId)
                .flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ALREADY_PART_MEMBER.toString()));
                    return Mono.just(part);
                });
    }

    private Mono<Part> checkAlreadyApply(Part part, Long userId) {
        return applyRepository.existsByPartIdAndUser(part.getId(), userId)
                .flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ALREADY_APPLY.toString()));
                    return Mono.just(part);
                });
    }

    @PostMapping("/report")
    public Mono<Boolean> reportProject(@RequestBody ReportInput input) {
        return projectRepository.existsById(input.projectId())
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString()));

                    return reportRepository.existsByProjectAndUser(input.projectId(), input.userId());
                })
                .flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ALREADY_REPORT.toString()));

                    return reportRepository.save(Report.builder()
                            .project(input.projectId())
                            .user(input.userId())
                            .reason(input.reason())
                            .build()
                    );
                })
                .thenReturn(true);
    }

    @PostMapping("/favorite")
    public Mono<Integer> updateFavorite(@RequestBody FavoriteUpdateInput input) {
        if (input.favorite() != -1 && input.favorite() != 1)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.INVALID_FAVORITE.toString()));

        return projectRepository.findById(input.project())
                .switchIfEmpty(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString())))
                .flatMap(project -> {
                    project.setFavorite(project.getFavorite() + input.favorite());
                    return projectRepository.save(project).thenReturn(project.getFavorite());
                });
    }
}
