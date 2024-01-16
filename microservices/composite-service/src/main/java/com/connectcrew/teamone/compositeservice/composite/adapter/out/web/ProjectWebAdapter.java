package com.connectcrew.teamone.compositeservice.composite.adapter.out.web;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.api.projectservice.leader.ApplyApiResponse;
import com.connectcrew.teamone.api.projectservice.leader.ApplyStatusApiResponse;
import com.connectcrew.teamone.api.projectservice.member.ApplyApiRequest;
import com.connectcrew.teamone.api.projectservice.member.MemberApiResponse;
import com.connectcrew.teamone.api.projectservice.project.*;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.SaveProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.UpdateProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.domain.*;
import com.connectcrew.teamone.compositeservice.global.error.adapter.out.WebClientExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<ProjectItem> findAllProjectItems(ProjectFilterOptionApiRequest option) {
        String[] host = this.host.replace("http://", "").split(":");

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(host[0].trim())
                        .port(Integer.parseInt(host[1].trim()))
                        .path("/project/list")
                        .queryParam("lastId", option.lastId())
                        .queryParam("size", option.size())
                        .queryParam("goal", option.goal() != null ? option.goal() : null)
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
                .bodyToFlux(ProjectItemApiResponse.class)
                .onErrorResume(exHandler::handleException)
                .map(ProjectItem::of);
    }

    @Override
    public Flux<ProjectItem> findAllProjectItems(Long userId) {
        return webClient.get()
                .uri(String.format("%s/project/%d", host, userId))
                .retrieve()
                .bodyToFlux(ProjectItemApiResponse.class)
                .onErrorResume(exHandler::handleException)
                .map(ProjectItem::of);
    }

    @Override
    public Mono<ProjectDetail> find(Long projectId, Long userId) {
        return webClient.get()
                .uri(String.format("%s/project/?id=%d&userId=%d", host, projectId, userId))
                .retrieve()
                .bodyToMono(ProjectApiResponse.class)
                .onErrorResume(exHandler::handleException)
                .map(ProjectDetail::of);
    }

    @Override
    public Flux<ProjectMember> findMembers(Long projectId) {

        return webClient.get()
                .uri(String.format("%s/members/%d", host, projectId))
                .retrieve()
                .bodyToFlux(MemberApiResponse.class)
                .onErrorResume(exHandler::handleException)
                .map(ProjectMember::of);
    }

    @Override
    public Flux<Apply> findAllApplies(Long userId, Long projectId, MemberPart part) {
        return webClient.get()
                .uri(String.format("%s/applies?userId=%d&projectId=%d&part=%s", host, userId, projectId, part.name()))
                .retrieve()
                .bodyToFlux(ApplyApiResponse.class)
                .map(Apply::of)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Flux<ApplyStatus> findAllApplyStatus(Long userId, Long projectId) {
        return webClient.get()
                .uri(String.format("%s/applyStatus?userId=%d&projectId=%d", host, userId, projectId))
                .retrieve()
                .bodyToFlux(ApplyStatusApiResponse.class)
                .map(ApplyStatus::of)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Long> save(CreateProjectApiRequest input) {
        return webClient.post()
                .uri(String.format("%s/project/", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Long.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Boolean> save(ApplyApiRequest input) {
        return webClient.post()
                .uri(String.format("%s/member/apply", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Boolean> save(ReportApiRequest input) {
        return webClient.post()
                .uri(String.format("%s/project/report", host))
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Integer> updateFavorite(ProjectFavoriteApiRequest favorite) {
        return webClient.post()
                .uri(String.format("%s/project/favorite", host))
                .bodyValue(favorite)
                .retrieve()
                .bodyToMono(Integer.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Long> update(UpdateProjectApiRequest project) {
        return webClient.put()
                .uri(String.format("%s/project/", host))
                .bodyValue(project)
                .retrieve()
                .bodyToMono(Long.class)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Apply> acceptApply(Long applyId, Long userId, String leaderMessage) {
        return webClient.post()
                .uri(String.format("%s/apply/%d/leader/%d/accept", host, applyId, userId))
                .bodyValue(leaderMessage)
                .retrieve()
                .bodyToMono(ApplyApiResponse.class)
                .map(Apply::of)
                .onErrorResume(exHandler::handleException);
    }

    @Override
    public Mono<Apply> rejectApply(Long applyId, Long userId, String leaderMessage) {
        return webClient.post()
                .uri(String.format("%s/apply/%d/leader/%d/reject", host, applyId, userId))
                .bodyValue(leaderMessage)
                .retrieve()
                .bodyToMono(ApplyApiResponse.class)
                .map(Apply::of)
                .onErrorResume(exHandler::handleException);
    }
}
