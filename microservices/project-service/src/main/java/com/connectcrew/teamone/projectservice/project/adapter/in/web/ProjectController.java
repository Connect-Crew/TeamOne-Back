package com.connectcrew.teamone.projectservice.project.adapter.in.web;

import com.connectcrew.teamone.projectservice.global.exceptions.application.port.in.SendErrorNotificationUseCase;
import com.connectcrew.teamone.projectservice.global.exceptions.enums.ErrorLevel;
import com.connectcrew.teamone.projectservice.project.adapter.in.web.request.CreateProjectRequest;
import com.connectcrew.teamone.projectservice.project.adapter.in.web.request.FavoriteRequest;
import com.connectcrew.teamone.projectservice.project.adapter.in.web.request.ProjectFilterOptionRequest;
import com.connectcrew.teamone.projectservice.project.adapter.in.web.request.ReportRequest;
import com.connectcrew.teamone.projectservice.project.adapter.in.web.response.ProjectItemResponse;
import com.connectcrew.teamone.projectservice.project.adapter.in.web.response.ProjectResponse;
import com.connectcrew.teamone.projectservice.project.application.port.in.CreateProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.QueryProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.UpdateProjectUseCase;
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
        return queryProjectUseCase.findAllByQuery(option.toQuery())
                .map(ProjectItemResponse::from)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectList", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/")
    public Mono<Long> createProject(@RequestBody CreateProjectRequest request) {
        return createProjectUseCase.create(request.toCommand())
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.createProject", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/")
    public Mono<ProjectResponse> findProject(Long id, Long userId) {
        return queryProjectUseCase.findById(id, userId)
                .map(tuple -> ProjectResponse.from(tuple.getT1(), tuple.getT2()))
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.findProject", ErrorLevel.ERROR, ex));
    }

    @GetMapping("/thumbnail")
    public Mono<String> getProjectThumbnail(Long id) {
        return queryProjectUseCase.findProjectThumbnail(id)
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.getProjectThumbnail", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/report")
    public Mono<Boolean> reportProject(@RequestBody ReportRequest request) {
        return createProjectUseCase.report(request.toCommand())
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.reportProject", ErrorLevel.ERROR, ex));
    }

    @PostMapping("/favorite")
    public Mono<Integer> updateFavorite(@RequestBody FavoriteRequest request) {
        return updateProjectUseCase.update(request.toCommand())
                .doOnError(ex -> sendErrorNotificationUseCase.send("ProjectController.updateFavorite", ErrorLevel.ERROR, ex));
    }
}
