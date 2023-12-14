package com.connectcrew.teamone.compositeservice.composite.adapter.out.web;

import com.connectcrew.teamone.compositeservice.global.exception.WebClientExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

@Repository
@RequiredArgsConstructor
public class ProjectWebAdapter {

    @Value("${app.project}")
    private String host;

    private final WebClient webClient;

    private final WebClientExceptionHandler exHandler;


}
