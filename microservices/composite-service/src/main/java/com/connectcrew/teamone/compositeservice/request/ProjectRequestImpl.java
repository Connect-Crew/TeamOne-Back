package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.api.project.ProjectDetail;
import com.connectcrew.teamone.api.project.ProjectFilterOption;
import com.connectcrew.teamone.api.project.ProjectInput;
import com.connectcrew.teamone.api.project.ProjectItem;
import com.connectcrew.teamone.compositeservice.exception.WebClientExceptionHandler;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProjectRequestImpl implements ProjectRequest {

    public final String host;

    private final WebClient webClient;

    private final WebClientExceptionHandler exHandler;

    public ProjectRequestImpl(String host, WebClient webClient) {
        this.host = host;
        this.webClient = webClient;
        this.exHandler = new WebClientExceptionHandler();
    }

    @Override
    public Flux<ProjectItem> getProjectList(ProjectFilterOption option) {
        return webClient.get()
                .uri(String.format("%s/list", host))
                .retrieve()
                .bodyToFlux(ProjectItem.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<ProjectDetail> getProjectDetail(Long projectId) {
        return webClient.get()
                .uri(String.format("%s/", host))
                .retrieve()
                .bodyToMono(ProjectDetail.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Long> saveProject(ProjectInput input) {
        return webClient.post()
                .uri(String.format("%s/", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Long.class)
                .onErrorResume(exHandler::handleException);
    }
}
