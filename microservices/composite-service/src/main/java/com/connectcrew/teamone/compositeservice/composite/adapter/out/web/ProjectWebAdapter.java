package com.connectcrew.teamone.compositeservice.composite.adapter.out.web;

import com.connectcrew.teamone.compositeservice.composite.adapter.out.web.response.MemberResponse;
import com.connectcrew.teamone.compositeservice.composite.adapter.out.web.response.ProjectDetailResponse;
import com.connectcrew.teamone.compositeservice.composite.adapter.out.web.response.ProjectItemResponse;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.SaveProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.UpdateProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.domain.*;
import com.connectcrew.teamone.compositeservice.composite.domain.vo.CreateProjectInfo;
import com.connectcrew.teamone.compositeservice.composite.domain.vo.ModifyProjectInfo;
import com.connectcrew.teamone.compositeservice.global.error.adapter.out.WebClientExceptionHandler;
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
                .uri(String.format("%s/project/thumbnail?id=%d", host, id))
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
                        .path("/project/list")
                        .queryParam("lastId", option.lastId())
                        .queryParam("size", option.size())
                        .queryParam("goal", option.goal() != null ? option.goal().name() : null)
                        .queryParam("career", option.career() != null ? option.career().name() : null)
                        .queryParam("region", option.region() != null ? option.region().stream().map(Enum::name).toList() : null)
                        .queryParam("online", option.online())
                        .queryParam("part", option.part())
                        .queryParam("skills", option.skills())
                        .queryParam("states", option.states() != null ? option.states().stream().map(Enum::name).toList() : null)
                        .queryParam("category", option.category())
                        .queryParam("search", option.search())
                        .build()
                )
                .retrieve()
                .bodyToFlux(ProjectItemResponse.class)
                .onErrorResume(exHandler::handleException)
                .map(ProjectItemResponse::toDomain);
    }

    @Override
    public Flux<ProjectItem> findAllProjectItems(Long userId) {
        return webClient.get()
                .uri(String.format("%s/project/%d", host, userId))
                .retrieve()
                .bodyToFlux(ProjectItemResponse.class)
                .onErrorResume(exHandler::handleException)
                .map(ProjectItemResponse::toDomain);
    }

    @Override
    public Mono<ProjectDetail> find(Long projectId, Long userId) {
        return webClient.get()
                .uri(String.format("%s/project/?id=%d&userId=%d", host, projectId, userId))
                .retrieve()
                .bodyToMono(ProjectDetailResponse.class)
                .onErrorResume(exHandler::handleException)
                .map(ProjectDetailResponse::toDomain);
    }

    @Override
    public Mono<List<ProjectMember>> findMembers(Long projectId) {
        ParameterizedTypeReference<List<MemberResponse>> type = new ParameterizedTypeReference<>() {
        };

        return webClient.get()
                .uri(String.format("%s/member/members?id=%d", host, projectId))
                .retrieve()
                .bodyToMono(type)
                .onErrorResume(exHandler::handleException)
                .map(members -> members.stream().map(MemberResponse::toDomain).toList());
    }

    @Override
    public Mono<Long> save(CreateProjectInfo input) {
        return webClient.post()
                .uri(String.format("%s/project/", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Long.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Boolean> save(Apply input) {
        return webClient.post()
                .uri(String.format("%s/member/apply", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Boolean> save(Report input) {
        return webClient.post()
                .uri(String.format("%s/project/report", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Integer> updateFavorite(ProjectFavorite favorite) {
        return webClient.post()
                .uri(String.format("%s/project/favorite", host))
                .bodyValue(favorite)
                .retrieve()
                .bodyToMono(Integer.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Long> update(ModifyProjectInfo project) {
        return webClient.put()
                .uri(String.format("%s/project/", host))
                .bodyValue(project)
                .retrieve()
                .bodyToMono(Long.class)
                .onErrorResume(exHandler::handleException);
    }
}
