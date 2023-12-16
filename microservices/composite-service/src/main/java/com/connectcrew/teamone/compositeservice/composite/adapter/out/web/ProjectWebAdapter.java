package com.connectcrew.teamone.compositeservice.composite.adapter.out.web;

import com.connectcrew.teamone.api.project.*;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.SaveProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.UpdateProjectOutput;
import com.connectcrew.teamone.compositeservice.global.exception.WebClientExceptionHandler;
import com.connectcrew.teamone.compositeservice.param.ProjectFavoriteParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectWebAdapter implements FindProjectOutput, SaveProjectOutput, UpdateProjectOutput {

    @Value("${app.project}")
    private String host;

    private final WebClient webClient;

    private final WebClientExceptionHandler exHandler;


    @Override
    public Mono<String> findProjectThumbnail(Long id) {
        return webClient.get()
                .uri(String.format("%s/thumbnail?id=%d", host, id))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Flux<ProjectItem> findAllProjectItems(ProjectFilterOption option) {
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
    public Mono<ProjectDetail> find(Long projectId, Long userId) {
        return webClient.get()
                .uri(String.format("%s/?id=%d&userId=%d", host, projectId, userId))
                .retrieve()
                .bodyToMono(ProjectDetail.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<List<ProjectMember>> findMembers(Long projectId) {
        ParameterizedTypeReference<List<ProjectMember>> type = new ParameterizedTypeReference<>() {
        };

        return webClient.get()
                .uri(String.format("%s/members?id=%d", host, projectId))
                .retrieve()
                .bodyToMono(type)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Long> save(ProjectInput input) {
        return webClient.post()
                .uri(String.format("%s/", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Long.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Boolean> save(ApplyInput input) {
        return webClient.post()
                .uri(String.format("%s/apply", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Boolean> save(ReportInput input) {
        return webClient.post()
                .uri(String.format("%s/report", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Integer> updateFavorite(ProjectFavoriteParam param) {
        return webClient.post()
                .uri(String.format("%s/favorite", host))
                .bodyValue(param)
                .retrieve()
                .bodyToMono(Integer.class)
                .onErrorResume(exHandler::handleException);
    }
}
