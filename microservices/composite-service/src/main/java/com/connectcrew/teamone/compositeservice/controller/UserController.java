package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.resposne.ProfileRes;
import com.connectcrew.teamone.compositeservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final JwtProvider jwtProvider;

    private final ProfileService profileService;

    @GetMapping("/myprofile")
    public Mono<ProfileRes> getMyProfile(@RequestHeader(JwtProvider.AUTH_HEADER) String token) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return profileService.getProfileRes(id);
    }

}
