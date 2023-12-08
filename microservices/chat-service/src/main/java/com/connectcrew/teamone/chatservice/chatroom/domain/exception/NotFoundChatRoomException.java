package com.connectcrew.teamone.chatservice.chatroom.domain.exception;

public class NotFoundChatRoomException extends RuntimeException {
    public NotFoundChatRoomException() {
    }

    public NotFoundChatRoomException(String message) {
        super(message);
    }

    public NotFoundChatRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundChatRoomException(Throwable cause) {
        super(cause);
    }
}
