package com.connectcrew.teamone.compositeservice.composite.application;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.QueryProjectUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.SaveProjectUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateProjectCommand;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.query.FindProjectListQuery;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.SaveProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.UpdateProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectAplService implements QueryProjectUseCase, SaveProjectUseCase {

    private final FindProjectOutput findProjectOutput;
    private final SaveProjectOutput saveProjectOutput;
    private final UpdateProjectOutput updateProjectOutput;

    @Override
    public Mono<List<ProjectItem>> getProjectList(FindProjectListQuery query) {
        log.trace("getProjectList: {}", query);
        return findProjectOutput.findAllProjectItems(query.toDomain()).collectList();
    }

    @Override
    public Mono<ProjectDetail> find(Long id, Long userId) {
        return findProjectOutput.find(id, userId);
    }

    @Override
    public Mono<List<ProjectMember>> getProjectMemberList(Long projectId) {
        return findProjectOutput.findMembers(projectId);
    }


    @Override
    public Mono<Long> save(CreateProjectCommand command) {
        return saveProjectOutput.save(command.toDomain());
    }

    @Override
    public Mono<Boolean> save(Apply apply) {
        return saveProjectOutput.save(apply);
    }

    @Override
    public Mono<Boolean> save(Report report) {
        return saveProjectOutput.save(report);
    }

    @Override
    public Mono<Integer> setFavorite(ProjectFavorite favorite) {
        return updateProjectOutput.updateFavorite(favorite);
    }
}
