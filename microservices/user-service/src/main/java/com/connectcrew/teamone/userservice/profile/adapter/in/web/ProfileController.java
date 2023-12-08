package com.connectcrew.teamone.userservice.profile.adapter.in.web;

import com.connectcrew.teamone.userservice.profile.adapter.in.web.response.ProfileResponse;
import com.connectcrew.teamone.userservice.profile.application.in.QueryProfileUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final QueryProfileUseCase queryProfileUseCase;

    @GetMapping("/")
    Mono<ProfileResponse> getProfile(Long id) {
        return queryProfileUseCase.findProfileByUserId(id)
                .map(ProfileResponse::from);
    }
}
