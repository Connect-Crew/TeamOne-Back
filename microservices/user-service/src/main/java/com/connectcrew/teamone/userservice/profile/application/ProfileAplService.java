package com.connectcrew.teamone.userservice.profile.application;

import com.connectcrew.teamone.api.exception.NotFoundException;
import com.connectcrew.teamone.userservice.profile.application.in.QueryProfileUseCase;
import com.connectcrew.teamone.userservice.profile.application.out.FindProfileOutput;
import com.connectcrew.teamone.userservice.profile.domain.Part;
import com.connectcrew.teamone.userservice.profile.domain.Profile;
import com.connectcrew.teamone.userservice.profile.domain.RepresentProject;
import com.connectcrew.teamone.userservice.profile.domain.enums.ProfileExceptionMessage;
import com.connectcrew.teamone.userservice.profile.domain.vo.FullProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileAplService implements QueryProfileUseCase {

    private final FindProfileOutput findProfileOutput;

    @Override
    public Mono<FullProfile> findProfileByUserId(Long userId) {
        return findProfileOutput.findByUserId(userId)
                .switchIfEmpty(Mono.error(new NotFoundException(ProfileExceptionMessage.NOTFOUND_PROFILE.getMessage())))
                .flatMap(profile -> {
                    Mono<List<Part>> partsMono = findProfileOutput.findAllPartByProfileId(userId).collectList();
                    Mono<List<RepresentProject>> representProjectIdsMono = findProfileOutput.finAllRepresentProjectIdByProfileId(userId).collectList();

                    return Mono.zip(partsMono, representProjectIdsMono)
                            .map(tuple -> new FullProfile(profile, tuple.getT1(), tuple.getT2()));
                });
    }

    @Override
    public Mono<String> findUserNameByUserId(Long userId) {
        return findProfileOutput.findByUserId(userId)
                .switchIfEmpty(Mono.error(new NotFoundException(ProfileExceptionMessage.NOTFOUND_PROFILE.getMessage())))
                .map(Profile::nickname);
    }
}
