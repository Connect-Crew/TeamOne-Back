package com.connectcrew.teamone.projectservice.project.application.port.in;

import com.connectcrew.teamone.projectservice.project.application.port.in.command.SaveProjectCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.SaveReportCommand;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.Report;
import reactor.core.publisher.Mono;

public interface SaveProjectUseCase {
    Mono<Project> saveProject(SaveProjectCommand command);

    Mono<Report> saveReport(SaveReportCommand command);
}
