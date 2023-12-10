package com.connectcrew.teamone.projectservice.project.application.port.out;

import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.Report;
import reactor.core.publisher.Mono;

public interface SaveProjectOutput {
    Mono<Project> create(Project project);

    Mono<Report> report(Report report);
}
