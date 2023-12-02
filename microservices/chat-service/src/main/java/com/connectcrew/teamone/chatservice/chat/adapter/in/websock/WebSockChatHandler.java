package com.connectcrew.teamone.chatservice.chat.adapter.in.websock;

import com.connectcrew.teamone.chatservice.chat.adapter.in.web.response.ChatResponse;
import com.connectcrew.teamone.chatservice.chat.adapter.in.websock.request.ChatRequest;
import com.connectcrew.teamone.chatservice.chat.application.port.in.CreateChatUseCase;
import com.connectcrew.teamone.chatservice.chat.domain.enums.ChatExceptionMessage;
import com.connectcrew.teamone.chatservice.chat.domain.enums.MessageType;
import com.connectcrew.teamone.chatservice.chat.domain.exception.UnauthorizedException;
import com.connectcrew.teamone.chatservice.chat.application.JwtValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSockChatHandler extends TextWebSocketHandler {

    private final CreateChatUseCase createChatUseCase;
    private final ObjectMapper objectMapper;
    private final JwtValidator jwtValidator;

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {
        ChatRequest chatMessage = getChatRequest(message);
        if (chatMessage == null) return;

        log.trace("chat payload: {}", chatMessage);
        try {
            String token = chatMessage.token();
            if (!jwtValidator.validateToken(token))
                throw new UnauthorizedException(ChatExceptionMessage.INVALID_TOKEN.getMessage());

            Long userId = jwtValidator.getId(token);

            createChatUseCase.createChat(chatMessage.roomId(), userId, chatMessage.message(), session);
        } catch (Exception e) {
            log.error("handleTextMessage error", e);
            sendError(session, e, chatMessage);
        }

    }

    private void sendError(WebSocketSession session, Exception e, ChatRequest chatMessage) {
        try {
            ChatResponse errorRes = new ChatResponse(MessageType.ERROR, 0L, chatMessage.roomId(), e.getMessage(), LocalDateTime.now());
            TextMessage errorMsg = new TextMessage(objectMapper.writeValueAsString(errorRes));
            session.sendMessage(errorMsg);
        } catch (IOException ex) {
            log.error("sendError error", ex);
        }

    }

    private ChatRequest getChatRequest(TextMessage message) {
        try {
            String payload = message.getPayload();

            return objectMapper.readValue(payload, ChatRequest.class);
        } catch (Exception e) {
            log.error("getChatRequest error", e);
            return null;
        }

    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
// TODO       chatService.removeUser(session);
    }
}
