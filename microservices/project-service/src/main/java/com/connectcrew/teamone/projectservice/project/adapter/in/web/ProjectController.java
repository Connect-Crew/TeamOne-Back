package com.connectcrew.teamone.projectservice.project.adapter.in.web;

import com.connectcrew.teamone.api.projectservice.project.*;
import com.connectcrew.teamone.api.userservice.notification.error.ErrorLevel;
import com.connectcrew.teamone.projectservice.global.exceptions.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.CreateProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.QueryProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.UpdateProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.CreateProjectCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.FavoriteCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.ReportCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.query.ProjectQuery;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final QueryProjectUseCase queryProjectUseCase;
    private final CreateProjectUseCase createProjectUseCase;
    private final UpdateProjectUseCase updateProjectUseCase;
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
    public Mono<Long> createProject(@RequestBody CreateProjectRequest request) {
        return createProjectUseCase.create(CreateProjectCommand.from(request))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.createProject", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/")
    public Mono<ProjectResponse> findProject(Long id, Long userId) {
        return queryProjectUseCase.findById(id, userId)
                .map(tuple -> tuple.getT1().toResponse(tuple.getT2()))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.findProject", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/thumbnail")
    public Mono<String> getProjectThumbnail(Long id) {
        return queryProjectUseCase.findProjectThumbnail(id)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectThumbnail", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/report")
    public Mono<Boolean> reportProject(@RequestBody ReportRequest request) {
        return createProjectUseCase.report(ReportCommand.from(request))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.reportProject", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/favorite")
    public Mono<Integer> updateFavorite(@RequestBody ProjectFavoriteRequest request) {
        return updateProjectUseCase.update(FavoriteCommand.from(request))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.updateFavorite", ErrorLevel.ERROR, ex));
    }
}
