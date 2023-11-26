package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.api.project.*;
import com.connectcrew.teamone.compositeservice.exception.WebClientExceptionHandler;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
        String[] host = this.host.replace("http://", "").split(":");

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(host[0].trim())
                        .port(Integer.parseInt(host[1].trim()))
                        .path("/list")
                        .queryParam("lastId", option.lastId())
                        .queryParam("size", option.size())
                        .queryParam("goal", option.goal())
                        .queryParam("career", option.career())
                        .queryParam("region", option.region())
                        .queryParam("online", option.online())
                        .queryParam("part", option.part())
                        .queryParam("skills", option.skills())
                        .queryParam("states", option.states())
                        .queryParam("category", option.category())
                        .queryParam("search", option.search())
                        .build()
                )
                .retrieve()
                .bodyToFlux(ProjectItem.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<ProjectDetail> getProjectDetail(Long projectId, Long userId) {
        return webClient.get()
                .uri(String.format("%s/?id=%d&userId=%d", host, projectId, userId))
                .retrieve()
                .bodyToMono(ProjectDetail.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<List<ProjectMember>> getProjectMembers(Long projectId) {
        ParameterizedTypeReference<List<ProjectMember>> type = new ParameterizedTypeReference<>() {
        };

        return webClient.get()
                .uri(String.format("%s/members?id=%d", host, projectId))
                .retrieve()
                .bodyToMono(type)
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

    @Override
    public Mono<Boolean> applyProject(ApplyInput input) {
        return webClient.post()
                .uri(String.format("%s/apply", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Boolean> reportProject(ReportInput input) {
        return webClient.post()
                .uri(String.format("%s/report", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Integer> updateFavorite(FavoriteUpdateInput input) {
        return webClient.post()
                .uri(String.format("%s/favorite", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Integer.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<String> getProjectThumbnail(Long projectId) {
        return webClient.get()
                .uri(String.format("%s/thumbnail?id=%d", host, projectId))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(exHandler::handleException);
    }
}
