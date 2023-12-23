package com.connectcrew.teamone.compositeservice.global.error.adapter.out;

import com.connectcrew.teamone.compositeservice.global.error.exception.InvalidOwnerException;
import com.connectcrew.teamone.compositeservice.global.error.exception.NotFoundException;
import com.connectcrew.teamone.compositeservice.global.error.domain.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;

@Slf4j
public class WebClientExceptionHandler {

    private final ObjectMapper mapper;

    public WebClientExceptionHandler() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule()); // 시간을 파싱하기 위해 사용
    }

    public <T> Mono<T> handleException(Throwable ex) {
        if (ex instanceof WebClientResponseException webEx) {
            if (webEx.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Mono.error(new NotFoundException(getErrorMessage(webEx)));
            } else if (webEx.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return Mono.error(new IllegalArgumentException(getErrorMessage(webEx)));
            } else if(webEx.getStatusCode() == HttpStatus.NOT_ACCEPTABLE) {
                return Mono.error(new InvalidOwnerException(getErrorMessage(webEx)));
            } else {
                log.warn("Unexpected error: {}", webEx.getStatusCode());
                log.warn("Error: {}", webEx.getResponseBodyAsString());
                return Mono.error(ex);
            }
        } else {
            log.error("An unexpected error occurred", ex);
            return Mono.error(new UnexpectedException("예상하지 못한 에러가 발생했습니다."));
        }
    }

    private String getErrorMessage(WebClientResponseException ex) {
        try {
            return mapper.readValue(new String(ex.getResponseBodyAsByteArray(), StandardCharsets.UTF_8), ErrorResponse.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }

}
