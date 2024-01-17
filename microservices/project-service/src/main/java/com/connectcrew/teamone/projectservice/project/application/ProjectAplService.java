package com.connectcrew.teamone.projectservice.project.application;

import com.connectcrew.teamone.api.exception.InvalidOwnerException;
import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.enums.ProjectState;
import com.connectcrew.teamone.projectservice.member.application.port.out.FindMemberOutput;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.project.application.port.in.QueryProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.SaveProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.UpdateProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.FavoriteCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.SaveProjectCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.SaveReportCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.UpdateProjectCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.query.ProjectQuery;
import com.connectcrew.teamone.projectservice.project.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.projectservice.project.application.port.out.SaveProjectOutput;
import com.connectcrew.teamone.projectservice.project.application.port.out.SendReportMessageOutput;
import com.connectcrew.teamone.projectservice.project.application.port.out.UpdateProjectOutput;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.ProjectPart;
import com.connectcrew.teamone.projectservice.project.domain.Report;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectAplService implements QueryProjectUseCase, SaveProjectUseCase, UpdateProjectUseCase {

    private final FindProjectOutput findProjectOutput;
    private final SaveProjectOutput saveProjectOutput;
    private final UpdateProjectOutput updateProjectOutput;
    private final FindMemberOutput findMemberOutput;
    private final SendReportMessageOutput sendReportMessageOutput;

    @Override
    public Flux<ProjectItem> findAllByQuery(ProjectQuery query) {
        return findProjectOutput.findAllByQuery(query.toOption());
    }

    @Override
    public Flux<ProjectItem> findAllByUserId(Long userId) {
        return findProjectOutput.findAllByUserId(userId);
    }

    @Override
    public Mono<String> findProjectThumbnail(Long projectId) {
        return findProjectOutput.findThumbnail(projectId);
    }

    @Override
    public Mono<Long> findLeaderByProject(Long projectId) {
        return findProjectOutput.findLeaderById(projectId);
    }

    @Override
    public Mono<Project> findById(Long projectId) {
        return findProjectOutput.findById(projectId);
    }

    @Override
    public Mono<Project> saveProject(SaveProjectCommand command) {
        return command.validate()
                .then(saveProjectOutput.save(command.toDomain()));
    }

    @Override
    public Mono<Report> saveReport(SaveReportCommand command) {
        return findProjectOutput.findTitleById(command.projectId())
                .switchIfEmpty(Mono.error(new NotFoundException("프로젝트를 찾을 수 없습니다. request: " + command)))
                .flatMap(title -> findProjectOutput.existsReportByProjectAndUser(command.projectId(), command.userId()).map(exists -> Tuples.of(title, exists)))
                .flatMap(tuples -> {
                    if (tuples.getT2()) return Mono.error(new IllegalArgumentException("이미 신고한 프로젝트입니다."));
                    return Mono.just(tuples.getT1());
                })
                .flatMap(title -> saveProjectOutput.report(command.toDomain(title)))
                .doOnNext(sendReportMessageOutput::send);
    }

    @Override
    public Mono<Integer> updateFavorite(FavoriteCommand command) {
        return updateProjectOutput.favorite(command.project(), command.favorite() ? 1 : -1);
    }

    @Override
    public Mono<Project> updateProject(UpdateProjectCommand command) {
        Mono<Project> project = findProjectOutput.findById(command.projectId())
                .switchIfEmpty(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString())));
        Mono<Member> leader = findMemberOutput.findByProjectAndUser(command.projectId(), command.userId())
                .switchIfEmpty(Mono.error(new NotFoundException("리더 정보를 찾을 수 없습니다.")));

        return project.flatMap(p -> leader.map(l -> Tuples.of(p, l)))
                .flatMap(tuple -> {
                    if (tuple.getT1().leader().equals(tuple.getT2().user())) return Mono.just(tuple);
                    return Mono.error(new InvalidOwnerException(ProjectExceptionMessage.INVALID_PROJECT_OWNER.toString()));
                })
                .flatMap(tuple -> command.validate().thenReturn(tuple))
                .map(tuple -> Tuples.of(tuple.getT1(), tuple.getT2(), command.toDomain(tuple.getT1(), tuple.getT2())))
                .flatMap(tuple -> validateUpdatedPart(tuple.getT1(), tuple.getT2(), tuple.getT3(), command.leaderParts()))
                .flatMap(saveProjectOutput::save);
    }

    @Override
    public Mono<ProjectState> updateProjectState(Long userId, Long projectId, ProjectState projectState) {
        return findProjectOutput.findById(projectId)
                .switchIfEmpty(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString())))
                .flatMap(project -> {
                    if (project.leader().equals(userId)) return Mono.just(project);
                    return Mono.error(new InvalidOwnerException(ProjectExceptionMessage.INVALID_PROJECT_OWNER.toString()));
                })
                .flatMap(project -> updateProjectOutput.updateState(projectId, projectState));
    }

    @Override
    public Mono<ProjectState> deleteProjectState(Long userId, Long projectId) {
        return findProjectOutput.findById(projectId)
                .switchIfEmpty(Mono.error(new NotFoundException(ProjectExceptionMessage.NOT_FOUND_PROJECT.toString())))
                .flatMap(project -> {
                    if (project.leader().equals(userId)) return Mono.just(project);
                    return Mono.error(new InvalidOwnerException(ProjectExceptionMessage.INVALID_PROJECT_OWNER.toString()));
                })
                .flatMap(project -> updateProjectOutput.updateState(projectId, ProjectState.DELETED));
    }

    @NotNull
    private Mono<Project> validateUpdatedPart(Project origin, Member originLeader, Project updated, List<MemberPart> updatedLeaderParts) {
        Set<Long> originPartIds = origin.parts().stream()
                .map(ProjectPart::id)
                .collect(Collectors.toSet());

        Set<Long> updatedPartIds = updated.parts().stream()
                .map(ProjectPart::id)
                .collect(Collectors.toSet());

        Set<Long> deletedPartIds = originPartIds.stream()
                .filter(id -> !updatedPartIds.contains(id))
                .collect(Collectors.toSet());

        // 삭제된 파트에 현재 인원이 있는지 검사
        for (ProjectPart part : origin.parts()) {
            if (!deletedPartIds.contains(part.id())) continue;
            if (part.current() <= 0) continue;
            // 인원이 1명있고, 리더이며, 직무를 수정한 경우
            if (part.current() == 1 && originLeader.containPart(part.part()) && !updatedLeaderParts.contains(part.part()))
                continue;


            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.UNREMOVABLE_PART.toString()));
        }

        for (ProjectPart part : updated.parts()) {
            if (part.current() > part.max()) { // 현재 인원이 max보다 많은지 검사
                return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.MAX_LESS_THAN_CURRENT.toString()));
            }
        }

        return Mono.just(updated);
    }
}
