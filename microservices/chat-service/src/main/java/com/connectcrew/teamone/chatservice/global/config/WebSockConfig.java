package com.connectcrew.teamone.chatservice.global.config;

import com.connectcrew.teamone.chatservice.chat.adapter.in.websock.WebSockChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSockConfig implements WebSocketConfigurer {
    private final WebSockChatHandler webSockChatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSockChatHandler, "/ws/chat").setAllowedOrigins("*");
    }
}
