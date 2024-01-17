package com.connectcrew.teamone.projectservice.project.adapter.in.web;

import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.api.projectservice.enums.ProjectState;
import com.connectcrew.teamone.api.projectservice.project.*;
import com.connectcrew.teamone.api.userservice.notification.error.ErrorLevel;
import com.connectcrew.teamone.projectservice.global.exceptions.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.QueryMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.SaveMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.UpdateMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.SaveMemberCommand;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.UpdateMemberCommand;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.project.application.port.in.QueryProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.SaveProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.UpdateProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.FavoriteCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.SaveProjectCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.SaveReportCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.UpdateProjectCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.query.ProjectQuery;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;
import com.connectcrew.teamone.projectservice.project.domain.vo.UserRelationWithProject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final QueryProjectUseCase queryProjectUseCase;
    private final SaveProjectUseCase saveProjectUseCase;
    private final UpdateProjectUseCase updateProjectUseCase;
    private final QueryMemberUseCase queryMemberUseCase;
    private final SaveMemberUseCase saveMemberUseCase;
    private final UpdateMemberUseCase updateMemberUseCase;
    private final SendErrorNotificationUseCase sendErrorNotificationUseCase;


    @GetMapping("/list")
    public Flux<ProjectItemApiResponse> getProjectList(ProjectFilterOptionApiRequest option) {
        log.trace("getProjectList - option: {}", option);
        return queryProjectUseCase.findAllByQuery(ProjectQuery.from(option))
                .map(ProjectItem::toResponse)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectList", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/list/{userId}")
    public Flux<ProjectItemApiResponse> getProjectList(@PathVariable Long userId) {
        log.trace("getProjectList - userId: {}", userId);
        return queryProjectUseCase.findAllByUserId(userId)
                .map(ProjectItem::toResponse)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectList", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/")
    @Transactional
    public Mono<Long> createProject(@RequestBody CreateProjectApiRequest request) {
        log.trace("createProject - request: {}", request);
        return saveProjectUseCase.saveProject(SaveProjectCommand.from(request))
                .doOnNext(project -> log.trace("createProject - project: {}", project))
                .flatMap(project -> saveMemberUseCase.saveMember(new SaveMemberCommand(project.leader(), project.id(), request.leaderParts()))
                        .doOnNext(member -> log.trace("createProject - member: {}", member))
                        .thenReturn(project))
                .map(Project::id)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.createProject", ErrorLevel.ERROR, ex));
    }

    @PutMapping("/")
    @Transactional
    public Mono<Long> updateProject(@RequestBody UpdateProjectApiRequest request) {
        log.trace("updateProject - request: {}", request);
        return updateProjectUseCase.updateProject(UpdateProjectCommand.from(request))
                .doOnNext(project -> log.trace("updateProject - project: {}", project))
                .flatMap(project -> updateMemberUseCase.updateMember(new UpdateMemberCommand(request.projectId(), project.leader(), request.leaderParts()))
                        .doOnNext(member -> log.trace("updateProject - member: {}", member))
                        .thenReturn(project))
                .map(Project::id)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.updateProject", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/state")
    @Transactional
    public Mono<ProjectState> updateProjectState(Long userId, Long projectId, ProjectState state) {
        log.trace("updateProjectState - userId: {}, projectId: {}, state: {}", userId, projectId, state);
        return updateProjectUseCase.updateProjectState(userId, projectId, state)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.updateProjectState", ErrorLevel.ERROR, ex));
    }

    @DeleteMapping("/")
    @Transactional
    public Mono<ProjectState> deleteProject(Long userId, Long projectId) {
        log.trace("deleteProject - userId: {}, projectId: {}", userId, projectId);
        return updateProjectUseCase.deleteProjectState(userId, projectId)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.deleteProject", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/")
    public Mono<ProjectApiResponse> findProject(Long id, Long userId) {
        log.trace("findProject - id: {}, userId: {}", id, userId);
        return queryProjectUseCase.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("존재하지 않는 프로젝트입니다.")))
                .doOnNext(project -> log.trace("findProject - project: {}", project))
                .flatMap(project -> {
                    Mono<UserRelationWithProject> userRelation = queryMemberUseCase.findUserRelationByProjectAndUser(id, userId)
                            .doOnNext(relation -> log.trace("findProject - relation: {}", relation));

                    Mono<Member> leader = queryMemberUseCase.findMemberByProjectAndUser(id, project.leader())
                            .doOnNext(l -> log.trace("findProject - leader: {}", l));

                    return Mono.zip(userRelation, leader)
                            .map(tuple -> project.toResponse(tuple.getT2(), tuple.getT1()));
                })
                .doOnNext(response -> log.trace("findProject - response: {}", response))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.findProject", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/thumbnail")
    public Mono<String> getProjectThumbnail(Long id) {
        log.trace("getProjectThumbnail - id: {}", id);
        return queryProjectUseCase.findProjectThumbnail(id)
                .switchIfEmpty(Mono.error(new NotFoundException("존재하지 않는 프로젝트입니다.")))
                .doOnNext(thumbnail -> log.trace("getProjectThumbnail - thumbnail: {}", thumbnail))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectThumbnail", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/report")
    public Mono<Boolean> reportProject(@RequestBody ReportApiRequest request) {
        log.trace("reportProject - request: {}", request);
        return saveProjectUseCase.saveReport(SaveReportCommand.from(request))
                .doOnNext(report -> log.trace("reportProject - report: {}", report))
                .thenReturn(true)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.reportProject", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/favorite")
    public Mono<Integer> updateFavorite(@RequestBody ProjectFavoriteApiRequest request) {
        log.trace("updateFavorite - request: {}", request);
        return updateProjectUseCase.updateFavorite(FavoriteCommand.from(request))
                .doOnNext(favorite -> log.trace("updateFavorite - favorite: {}", favorite))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.updateFavorite", ErrorLevel.ERROR, ex));
    }
}
