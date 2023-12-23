package com.connectcrew.teamone.compositeservice.global.error.exception;

public class InvalidOwnerException extends Exception {
    public InvalidOwnerException() {
    }

    public InvalidOwnerException(String message) {
        super(message);
    }

    public InvalidOwnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOwnerException(Throwable cause) {
        super(cause);
    }
}
