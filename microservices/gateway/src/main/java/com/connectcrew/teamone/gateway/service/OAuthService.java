package com.connectcrew.teamone.gateway.service;

import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.gateway.dto.OAuthAttributes;
import com.connectcrew.teamone.gateway.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OAuthService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserService userService;

    @Override
    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본으로 제공되는 Oauth2UserService를 이용해 OAuth2User를 가져온다.
        DefaultReactiveOAuth2UserService delegate = new DefaultReactiveOAuth2UserService();
        return delegate.loadUser(userRequest)
                .map(oAuth2User -> {
                    String registrationId = userRequest.getClientRegistration().getRegistrationId();
                    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

                    return OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
                })
                .flatMap(attribute -> userService.getUser(attribute.getSocialId(), attribute.getSocial()).map(user -> Tuples.of(attribute, user)))
                .map(tuple -> {
                    OAuthAttributes attributes = tuple.getT1();
                    User user = tuple.getT2();

                    attributes.getAttributes().put("user", new SessionUser(user));

                    return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.role().name())), attributes.getAttributes(), attributes.getNameAttributeKey());
                });
    }
}
