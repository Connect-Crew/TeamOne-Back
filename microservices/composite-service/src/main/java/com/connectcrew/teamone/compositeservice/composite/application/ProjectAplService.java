package com.connectcrew.teamone.compositeservice.composite.application;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.enums.ProjectState;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.QueryProjectUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.SaveProjectUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.UpdateProjectUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateProjectCommand;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.ModifyProjectCommand;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.query.FindProjectListQuery;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.SaveProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.UpdateProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectAplService implements QueryProjectUseCase, UpdateProjectUseCase, SaveProjectUseCase {

    private final FindProjectOutput findProjectOutput;
    private final SaveProjectOutput saveProjectOutput;
    private final UpdateProjectOutput updateProjectOutput;

    @Override
    public Mono<List<ProjectItem>> getProjectList(FindProjectListQuery query) {
        log.trace("getProjectList: {}", query);
        return findProjectOutput.findAllProjectItems(query.toApiRequest()).collectList();
    }

    @Override
    public Mono<List<ProjectItem>> getProjectList(Long userId) {
        log.trace("getProjectList: {}", userId);
        return findProjectOutput.findAllProjectItems(userId).collectList();
    }

    @Override
    public Mono<ProjectDetail> find(Long id, Long userId) {
        return findProjectOutput.find(id, userId);
    }

    @Override
    public Flux<ProjectMember> getProjectMemberList(Long projectId) {
        return findProjectOutput.findMembers(projectId);
    }

    @Override
    public Flux<Apply> getApplies(Long userId, Long projectId, MemberPart part) {
        return findProjectOutput.findAllApplies(userId, projectId, part);
    }

    @Override
    public Flux<ApplyStatus> getApplyStatus(Long userId, Long projectId) {
        return findProjectOutput.findAllApplyStatus(userId, projectId);
    }


    @Override
    public Mono<Long> save(CreateProjectCommand command) {
        return saveProjectOutput.save(command.toApiRequest());
    }

    @Override
    public Mono<Boolean> save(Apply apply) {
        return saveProjectOutput.save(apply.toApiRequest());
    }

    @Override
    public Mono<Boolean> save(Report report) {
        return saveProjectOutput.save(report.toApiRequest());
    }

    @Override
    public Mono<Integer> setFavorite(ProjectFavorite favorite) {
        return updateProjectOutput.updateFavorite(favorite.toApiRequest());
    }

    @Override
    public Mono<Long> update(ModifyProjectCommand command) {
        return updateProjectOutput.update(command.toApiRequest());
    }

    @Override
    public Mono<ProjectState> updateProjectState(Long userId, Long projectId, ProjectState projectState) {
        return updateProjectOutput.updateState(userId, projectId, projectState);
    }

    @Override
    public Mono<ProjectState> deleteProjectState(Long userId, Long projectId) {
        return updateProjectOutput.delete(userId, projectId);
    }

    @Override
    public Mono<Apply> acceptApply(Long applyId, Long userId, String leaderMessage) {
        return updateProjectOutput.acceptApply(applyId, userId, leaderMessage);
    }

    @Override
    public Mono<Apply> rejectApply(Long applyId, Long userId, String leaderMessage) {
        return updateProjectOutput.rejectApply(applyId, userId, leaderMessage);
    }
}
