package com.connectcrew.teamone.api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorInfo {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;
    private final String path;
    private final Integer status;
    private final String error;
    private final String message;

    // Object Mapping에 사용
    public ErrorInfo() {
        timestamp = null;
        status = null;
        error = null;
        path = null;
        message = null;
    }

    public ErrorInfo(HttpStatus httpStatus, String path, String message) {
        timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.path = path;
        this.message = message;
    }
}
