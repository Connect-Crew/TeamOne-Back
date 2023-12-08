package com.connectcrew.teamone.chatservice.chatroom.domain.exception;

public class NotRegisteredChatRoomException extends RuntimeException{
    public NotRegisteredChatRoomException() {
    }

    public NotRegisteredChatRoomException(String message) {
        super(message);
    }

    public NotRegisteredChatRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotRegisteredChatRoomException(Throwable cause) {
        super(cause);
    }
}
