package com.connectcrew.teamone.compositeservice.request;

import com.connectcrew.teamone.api.user.auth.Social;
import com.connectcrew.teamone.api.user.auth.User;
import com.connectcrew.teamone.api.user.auth.param.UserInputParam;
import com.connectcrew.teamone.api.user.profile.Profile;
import reactor.core.publisher.Mono;

public interface UserRequest {
    Mono<User> getUser(String socialId, Social provider);

    Mono<User> saveUser(UserInputParam user);

    Mono<Profile> getProfile(Long id);
}
