package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.exception.ErrorInfo;
import com.connectcrew.teamone.compositeservice.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(value = {
            IllegalArgumentException.class,
    })
    public Mono<ResponseEntity<ErrorInfo>> handleBadRequest(ServerWebExchange exchange, Exception ex) {
        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, exchange, ex);
    }

    @ExceptionHandler(value = {
            UnauthorizedException.class,
    })
    public Mono<ResponseEntity<ErrorInfo>> handleUnauthorized(ServerWebExchange exchange, Exception ex) {
        return createHttpErrorInfo(HttpStatus.UNAUTHORIZED, exchange, ex);
    }

    @ExceptionHandler({
            Exception.class,
    })
    public Mono<ResponseEntity<ErrorInfo>> handleConflict(ServerWebExchange exchange, Exception ex) {
        return createHttpErrorInfo(HttpStatus.CONFLICT, exchange, ex);
    }

    private Mono<ResponseEntity<ErrorInfo>> createHttpErrorInfo(HttpStatus httpStatus, ServerWebExchange exchange, Exception ex) {
        final String path = exchange.getRequest().getPath().toString();
        final String message = ex.getMessage();

        log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
        return Mono.just(ResponseEntity.status(httpStatus).body(new ErrorInfo(httpStatus, path, message)));
    }
}
