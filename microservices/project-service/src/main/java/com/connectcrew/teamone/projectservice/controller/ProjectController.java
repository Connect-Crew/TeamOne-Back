package com.connectcrew.teamone.projectservice.controller;

import com.connectcrew.teamone.api.exception.InvalidOwnerException;
import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.api.project.*;
import com.connectcrew.teamone.api.project.values.Career;
import com.connectcrew.teamone.api.project.values.ProjectGoal;
import com.connectcrew.teamone.api.project.values.ProjectState;
import com.connectcrew.teamone.api.project.values.Region;
import com.connectcrew.teamone.projectservice.entity.*;
import com.connectcrew.teamone.projectservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final BannerRepository bannerRepository;

    private final PartRepository partRepository;

    private final SkillRepository skillRepository;

    private final CategoryRepository categoryRepository;

    private final MemberRepository memberRepository;

    @PostMapping("/")
    @Transactional
    public Mono<Long> createProject(@RequestBody ProjectInput input) {
        // TODO input validation

        return projectRepository.save(inputToProject(input))
                .flatMap(p -> {
                    Long id = p.getId();

                    Mono<Long> saveBanners = saveBanners(input, id);
                    Mono<Long> savePartsAndMember = saveParts(input, id)
                            .flatMap(parts -> saveLeaderMember(input, id, parts));
                    Mono<Long> saveSkills = saveSkills(input, id);
                    Mono<Long> saveCategories = saveCategories(input, id);


                    return Mono.zip(saveBanners, savePartsAndMember, saveSkills, saveCategories)
                            .map(tuple -> id);
                });

    }

    private Project inputToProject(ProjectInput input) {
        Career careerMin = Career.valueOf(input.careerMin());
        Career careerMax = Career.valueOf(input.careerMax());

        return Project.builder()
                .title(input.title())
                .introduction(input.introduction())
                .memberIntroduction(input.membersIntroduction())
                .careerMin(careerMin.getId())
                .careerMax(careerMax.getId())
                .leader(input.leader())
                .withOnline(input.online())
                .region(input.region().name())
                .createdAt(LocalDateTime.now())
                .startDate(input.start())
                .endDate(input.end())
                .state(input.state().name())
                .goal(input.goal().name())
                .favorite(0)
                .build();
    }

    @NotNull
    private Mono<Long> saveCategories(ProjectInput input, Long id) {
        List<Category> categories = new ArrayList<>();
        for (String category : input.category()) {
            categories.add(Category.builder()
                    .project(id)
                    .category(category)
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
                    .skill(skill)
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
                    .part(recruit.part())
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
        List<String> leaderParts = input.leaderParts();
        List<Member> members = new ArrayList<>();
        for (Part part : parts) {
            if (!leaderParts.contains(part.getPart())) continue;

            Member member = Member.builder()
                    .part_id(part.getId())
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
        // TODO Input validation

        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("프로젝트를 찾을 수 없습니다.")))
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
                                Map<Long, String> partMap = new HashMap<>();
                                for (Part p : tuple.getT2()) {
                                    partMap.put(p.getId(), p.getPart());
                                    recruits.add(new RecruitStatus(p.getPart(), p.getComment(), p.getCollected(), p.getTargetCollect()));
                                }

                                List<String> skillNames = tuple.getT3().stream().map(Skill::getSkill).toList();

                                List<String> categoryNames = tuple.getT4().stream()
                                        .map(Category::getCategory).toList();

                                List<ProjectMember> projectMembers = tuple.getT5().stream()
                                        .collect(Collectors.groupingBy(Member::getUser, Collectors.mapping(m -> partMap.get(m.getPart_id()), Collectors.toList())))
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
                                        .startDate(project.getStartDate())
                                        .endDate(project.getEndDate())
                                        .state(ProjectState.valueOf(project.getState()))
                                        .careerMin(Career.valueOf(project.getCareerMin()))
                                        .careerMax(Career.valueOf(project.getCareerMax()))
                                        .category(categoryNames)
                                        .goal(ProjectGoal.valueOf(project.getGoal()))
                                        .leader(project.getLeader())
                                        .introduction(project.getIntroduction())
                                        .favorite(project.getFavorite())
                                        .recruitStatuses(recruits)
                                        .membersIntroduction(project.getMemberIntroduction())
                                        .members(projectMembers)
                                        .skills(skillNames)
                                        .build();
                            });
                });
    }

    @DeleteMapping("/")
    public Mono<Long> deleteProject(Long id, Long leader) {
        // TODO Input validation

        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("프로젝트를 찾을 수 없습니다.")))
                .flatMap(project -> {
                    if (!project.getLeader().equals(leader))
                        return Mono.error(new InvalidOwnerException("프로젝트의 리더가 아닙니다."));

                    return projectRepository.delete(project).thenReturn(project.getId());
                });
    }
}
