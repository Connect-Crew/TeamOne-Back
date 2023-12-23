package com.connectcrew.teamone.projectservice.project.adapter.out.persistence;

import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.api.project.values.MemberPart;
import com.connectcrew.teamone.api.project.values.ProjectCategory;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.*;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository.*;
import com.connectcrew.teamone.projectservice.project.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.projectservice.project.application.port.out.SaveProjectOutput;
import com.connectcrew.teamone.projectservice.project.application.port.out.UpdateProjectOutput;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;
import com.connectcrew.teamone.projectservice.project.domain.Report;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements FindProjectOutput, SaveProjectOutput, UpdateProjectOutput {

    private final CustomRepository customRepository;
    private final ProjectRepository projectRepository;
    private final BannerRepository bannerRepository;
    private final PartRepository partRepository;
    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    private final ReportRepository reportRepository;

    @Override
    public Flux<ProjectItem> findAllByQuery(ProjectOption option) {
        return customRepository.findAllByOption(option)
                .flatMap(project -> findThumbnail(project.id()).map(banner -> Tuples.of(project, banner)))
                .flatMap(tuple -> findAllRecruitByProject(tuple.getT1().id()).map(parts -> Tuples.of(tuple.getT1(), tuple.getT2(), parts)))
                .map(tuple -> tuple.getT1().toItem(tuple.getT2(), tuple.getT3()));
    }

    private Mono<BannerEntity> findThumbnail(Long project) {
        return bannerRepository.findFirstByProjectOrderByIdx(project)
                .defaultIfEmpty(new BannerEntity());
    }

    private Mono<List<RecruitStatus>> findAllRecruitByProject(Long project) {
        return partRepository.findAllByProject(project)
                .map(PartEntity::toDomain)
                .collectList()
                .defaultIfEmpty(List.of());
    }

    @Override
    public Mono<Project> create(Project project) {
        return projectRepository.save(ProjectEntity.from(project))
                .flatMap(p -> saveBanners(project, p.getId()).map(b -> Tuples.of(p, b)))
                .flatMap(tuple -> saveParts(project, tuple.getT1().getId()).map(p -> Tuples.of(tuple.getT1(), tuple.getT2(), p)))
                .flatMap(tuple -> saveCategories(project, tuple.getT1().getId()).map(c -> Tuples.of(tuple.getT1(), tuple.getT2(), tuple.getT3(), c)))
                .flatMap(tuple -> saveSkills(project, tuple.getT1().getId()).map(s -> Tuples.of(tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4(), s)))
                .map(tuple -> tuple.getT1().toDomain(getBannerPaths(tuple.getT2()), getRecruitStatuses(tuple.getT3()), getSkills(tuple.getT5()), getCategories(tuple.getT4())))
                .onErrorResume(e -> {
                    log.error("createProject - error: {}", e.getMessage(), e);
                    return Mono.error(new RuntimeException(ProjectExceptionMessage.CREATE_PROJECT_FAILED.toString()));
                });
    }

    @Override
    public Mono<Report> report(Report report) {
        return projectRepository.existsById(report.projectId())
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString()));

                    return reportRepository.existsByProjectAndUser(report.projectId(), report.userId());
                })
                .flatMap(exists -> {
                    if (exists)
                        return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ALREADY_REPORT.toString()));

                    return reportRepository.save(ReportEntity.from(report));
                })
                .thenReturn(report);
    }

    @NotNull
    private Mono<List<BannerEntity>> saveBanners(Project project, Long id) {
        return bannerRepository.saveAll(BannerEntity.from(project, id))
                .collectList();
    }

    @NotNull
    private Mono<List<PartEntity>> saveParts(Project project, Long id) {
        return partRepository.saveAll(PartEntity.from(project, id))
                .collectList();
    }

    @NotNull
    private Mono<List<CategoryEntity>> saveCategories(Project project, Long id) {
        return categoryRepository.saveAll(CategoryEntity.from(project, id))
                .collectList();
    }

    @NotNull
    private Mono<List<SkillEntity>> saveSkills(Project project, Long id) {
        return skillRepository.saveAll(SkillEntity.from(project, id))
                .collectList();
    }

    @Override
    public Mono<Project> findById(Long id) {
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString())))
                .flatMap(project -> {
                    Mono<List<BannerEntity>> banners = bannerRepository.findAllByProject(id).collectList();
                    Mono<List<PartEntity>> parts = partRepository.findAllByProject(id).collectList();
                    Mono<List<SkillEntity>> skills = skillRepository.findAllByProject(id).collectList();
                    Mono<List<CategoryEntity>> categories = categoryRepository.findAllByProject(id).collectList();

                    return Mono.zip(banners, parts, skills, categories)
                            .map(tuple -> {
                                List<String> bannerPaths = getBannerPaths(tuple.getT1());
                                List<RecruitStatus> recruitStatuses = getRecruitStatuses(tuple.getT2());
                                List<String> skillsList = getSkills(tuple.getT3());
                                List<ProjectCategory> categoriesList = getCategories(tuple.getT4());

                                return project.toDomain(bannerPaths, recruitStatuses, skillsList, categoriesList);
                            });
                });
    }

    @Override
    public Mono<String> findProjectThumbnail(Long id) {
        return bannerRepository.findFirstByProjectOrderByIdx(id)
                .map(BannerEntity::getPath);
    }

    @Override
    public Mono<Boolean> existsProjectById(Long id) {
        return projectRepository.existsById(id);
    }

    @Override
    public Mono<RecruitStatus> findByProjectAndPart(Long project, MemberPart part) {
        return partRepository.findByProjectAndPart(project, part.name())
                .map(PartEntity::toDomain);
    }

    private List<String> getBannerPaths(List<BannerEntity> banners) {
        return banners.stream()
                .sorted(Comparator.comparingInt(BannerEntity::getIdx))
                .map(BannerEntity::getPath).toList();
    }

    private List<RecruitStatus> getRecruitStatuses(List<PartEntity> parts) {
        return parts.stream()
                .map(PartEntity::toDomain)
                .toList();
    }

    private List<String> getSkills(List<SkillEntity> skills) {
        return skills.stream()
                .map(SkillEntity::getName)
                .toList();
    }

    private List<ProjectCategory> getCategories(List<CategoryEntity> categories) {
        return categories.stream()
                .map(CategoryEntity::getName)
                .map(ProjectCategory::valueOf)
                .toList();
    }

    @Override
    public Mono<Integer> updateFavorite(Long project, Integer change) {
        return projectRepository.findById(project)
                .switchIfEmpty(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString())))
                .flatMap(entity -> {
                    Integer favorite = entity.getFavorite() + change;
                    if (favorite < 0)
                        return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.FAVORITE_NEGATIVE.toString()));

                    entity.setFavorite(favorite);
                    return projectRepository.save(entity).thenReturn(favorite);
                });
    }
}
