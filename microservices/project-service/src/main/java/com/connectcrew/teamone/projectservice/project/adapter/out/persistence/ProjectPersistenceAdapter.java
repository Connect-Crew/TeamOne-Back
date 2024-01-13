package com.connectcrew.teamone.projectservice.project.adapter.out.persistence;

import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.*;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository.*;
import com.connectcrew.teamone.projectservice.project.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.projectservice.project.application.port.out.SaveProjectOutput;
import com.connectcrew.teamone.projectservice.project.application.port.out.UpdateProjectOutput;
import com.connectcrew.teamone.projectservice.project.domain.*;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Objects;

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
                .flatMap(project -> findThumbnailEntity(project.id()).map(banner -> Tuples.of(project, banner)))
                .flatMap(tuple -> findAllRecruitByProject(tuple.getT1().id()).map(parts -> Tuples.of(tuple.getT1(), tuple.getT2(), parts)))
                .map(tuple -> tuple.getT1().toItem(tuple.getT2(), tuple.getT3()));
    }

    @Override
    public Flux<ProjectItem> findAllByUserId(Long userId) {
        return customRepository.findAllByUserId(userId)
                .flatMap(project -> findThumbnailEntity(project.id()).map(banner -> Tuples.of(project, banner)))
                .flatMap(tuple -> findAllRecruitByProject(tuple.getT1().id()).map(parts -> Tuples.of(tuple.getT1(), tuple.getT2(), parts)))
                .map(tuple -> tuple.getT1().toItem(tuple.getT2(), tuple.getT3()));
    }

    private Mono<BannerEntity> findThumbnailEntity(Long project) {
        return bannerRepository.findFirstByProjectOrderByIdx(project)
                .defaultIfEmpty(new BannerEntity());
    }

    @Override
    public Mono<Project> findById(Long id) {
        return projectRepository.findById(id)
                .flatMap(project -> {
                    Mono<List<Banner>> banners = bannerRepository.findAllByProject(id)
                            .map(BannerEntity::toDomain)
                            .collectList();
                    Mono<List<ProjectPart>> parts = partRepository.findAllByProject(id)
                            .map(PartEntity::toDomain)
                            .collectList();
                    Mono<List<Skill>> skills = skillRepository.findAllByProject(id)
                            .map(SkillEntity::toDomain)
                            .collectList();
                    Mono<List<Category>> categories = categoryRepository.findAllByProject(id)
                            .map(CategoryEntity::toDomain)
                            .collectList();

                    return Mono.zip(banners, parts, skills, categories)
                            .map(tuple -> project.toDomain(tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4()));
                });
    }

    @Override
    public Mono<Long> findLeaderById(Long projectId) {
        return projectRepository.findById(projectId).map(ProjectEntity::getLeader);
    }

    @Override
    public Mono<String> findTitleById(Long projectId) {
        return projectRepository.findById(projectId).map(ProjectEntity::getTitle);
    }

    @Override
    public Mono<String> findThumbnail(Long projectId) {
        return bannerRepository.findFirstByProjectOrderByIdx(projectId)
                .map(BannerEntity::getPath);
    }

    @Override
    public Mono<Boolean> existsById(Long projectId) {
        return projectRepository.existsById(projectId);
    }

    @Override
    public Mono<Boolean> existsReportByProjectAndUser(Long project, Long user) {
        return reportRepository.existsByProjectAndUser(project, user);
    }

    @Override
    public Flux<ProjectPart> findAllProjectPartByProject(Long project) {
        return partRepository.findAllByProject(project)
                .map(PartEntity::toDomain);
    }

    @Override
    public Mono<ProjectPart> findProjectPartByProjectAndPart(Long project, MemberPart part) {
        return partRepository.findByProjectAndPart(project, part.name())
                .map(PartEntity::toDomain);
    }

    private Mono<List<ProjectPart>> findAllRecruitByProject(Long project) {
        return partRepository.findAllByProject(project)
                .map(PartEntity::toDomain)
                .collectList()
                .defaultIfEmpty(List.of());
    }

    @Override
    public Mono<Project> save(Project project) {
        List<Long> bannerIds = project.banners().stream()
                .map(Banner::id)
                .filter(Objects::nonNull)
                .toList();

        List<Long> partIds = project.parts().stream()
                .map(ProjectPart::id)
                .filter(Objects::nonNull)
                .toList();

        List<Long> categoryIds = project.category().stream()
                .map(Category::id)
                .filter(Objects::nonNull)
                .toList();

        List<Long> skillIds = project.skills().stream()
                .map(Skill::id)
                .filter(Objects::nonNull)
                .toList();

        // 삭제된 배너, 파트, 카테고리, 스킬 삭제
        return bannerRepository.deleteAllByProjectAndIdNotIn(project.id(), bannerIds)
                .thenMany(partRepository.deleteAllByProjectAndIdNotIn(project.id(), partIds))
                .thenMany(categoryRepository.deleteAllByProjectAndIdNotIn(project.id(), categoryIds))
                .thenMany(skillRepository.deleteAllByProjectAndIdNotIn(project.id(), skillIds))
                .then()
                // 프로젝트 저장
                .then(projectRepository.save(ProjectEntity.from(project)))
                .flatMap(p -> {
                    Mono<List<Banner>> banners = bannerRepository.saveAll(BannerEntity.from(project.banners(), p.getId()))
                            .map(BannerEntity::toDomain)
                            .collectList();

                    Mono<List<ProjectPart>> parts = partRepository.saveAll(PartEntity.from(project.parts(), p.getId()))
                            .map(PartEntity::toDomain)
                            .collectList();

                    Mono<List<Skill>> skills = skillRepository.saveAll(SkillEntity.from(project.skills(), p.getId()))
                            .map(SkillEntity::toDomain)
                            .collectList();

                    Mono<List<Category>> categories = categoryRepository.saveAll(CategoryEntity.from(project.category(), p.getId()))
                            .map(CategoryEntity::toDomain)
                            .collectList();

                    return banners.flatMap(b -> parts.map(pt -> Tuples.of(b, pt)))
                            .flatMap(tuple -> skills.map(s -> Tuples.of(tuple.getT1(), tuple.getT2(), s)))
                            .flatMap(tuple -> categories.map(c -> Tuples.of(tuple.getT1(), tuple.getT2(), tuple.getT3(), c)))
                            .map(tuple -> p.toDomain(tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4()));
                })
                .onErrorResume(e -> {
                    log.error("createProject - error: {}", e.getMessage(), e);
                    return Mono.error(new RuntimeException(ProjectExceptionMessage.UPDATE_PROJECT_FAILED.toString()));
                });
    }

    @Override
    public Mono<Report> report(Report report) {
        return reportRepository.save(ReportEntity.from(report))
                .map(entity -> entity.toDomain(report.projectTitle()));
    }



    @Override
    public Mono<Integer> favorite(Long project, Integer change) {
        return projectRepository.findById(project)
                .flatMap(entity -> {
                    Integer favorite = entity.getFavorite() + change;
                    if (favorite < 0)
                        return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.FAVORITE_NEGATIVE.toString()));

                    entity.setFavorite(favorite);
                    return projectRepository.save(entity).thenReturn(favorite);
                });
    }
}
