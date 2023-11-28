package com.connectcrew.teamone.chatservice.request;

import com.connectcrew.teamone.chatservice.model.ChatRoom;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

public class ProjectRequestImpl implements ProjectRequest {

    public final String host;

    private final RestTemplate restTemplate;

    public ProjectRequestImpl(String host, RestTemplate restTemplate) {
        this.host = host;
        this.restTemplate = restTemplate;
    }


    @Override
    public Set<ChatRoom> getChatRooms(Long userId) {
        ParameterizedTypeReference<Set<ChatRoom>> responseType = new ParameterizedTypeReference<>() {
        };

        return restTemplate.exchange(String.format("%s/chat?userId=%d", host, userId), HttpMethod.GET, null, responseType).getBody();
    }
}
