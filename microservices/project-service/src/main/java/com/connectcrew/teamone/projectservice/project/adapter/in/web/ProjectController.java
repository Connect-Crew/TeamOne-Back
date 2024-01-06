package com.connectcrew.teamone.projectservice.project.adapter.in.web;

import com.connectcrew.teamone.api.projectservice.project.*;
import com.connectcrew.teamone.api.userservice.notification.error.ErrorLevel;
import com.connectcrew.teamone.projectservice.global.exceptions.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.QueryMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.SaveMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.UpdateMemberUseCase;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.SaveMemberCommand;
import com.connectcrew.teamone.projectservice.member.application.port.in.command.UpdateMemberCommand;
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
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<ProjectItemResponse> getProjectList(ProjectFilterOptionRequest option) {
        return queryProjectUseCase.findAllByQuery(ProjectQuery.from(option))
                .map(ProjectItem::toResponse)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectList", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/list/{userId}")
    public Flux<ProjectItemResponse> getProjectList(@PathVariable Long userId) {
        return queryProjectUseCase.findAllByUserId(userId)
                .map(ProjectItem::toResponse)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectList", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/")
    @Transactional
    public Mono<Long> createProject(@RequestBody CreateProjectRequest request) {
        return saveProjectUseCase.saveProject(SaveProjectCommand.from(request))
                .flatMap(project -> saveMemberUseCase.saveMember(new SaveMemberCommand(project.leader(), project.id(), request.leaderParts())).thenReturn(project))
                .map(Project::id)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.createProject", ErrorLevel.ERROR, ex));
    }

    @PutMapping("/")
    @Transactional
    public Mono<Long> updateProject(@RequestBody UpdateProjectRequest request) {
        return updateProjectUseCase.updateProject(UpdateProjectCommand.from(request))
                .flatMap(project -> updateMemberUseCase.updateMember(new UpdateMemberCommand(request.projectId(), project.leader(), request.leaderParts())).thenReturn(project))
                .map(Project::id)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.updateProject", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/")
    public Mono<ProjectResponse> findProject(Long id, Long userId) {
        return queryProjectUseCase.findById(id)
                .flatMap(project -> queryMemberUseCase.findUserRelationByProjectAndUser(id, userId).map(project::toResponse))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.findProject", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/thumbnail")
    public Mono<String> getProjectThumbnail(Long id) {
        return queryProjectUseCase.findProjectThumbnail(id)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectThumbnail", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/report")
    public Mono<Boolean> reportProject(@RequestBody ReportRequest request) {
        return saveProjectUseCase.saveReport(SaveReportCommand.from(request))
                .thenReturn(true)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.reportProject", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/favorite")
    public Mono<Integer> updateFavorite(@RequestBody ProjectFavoriteRequest request) {
        return updateProjectUseCase.updateFavorite(FavoriteCommand.from(request))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.updateFavorite", ErrorLevel.ERROR, ex));
    }
}
