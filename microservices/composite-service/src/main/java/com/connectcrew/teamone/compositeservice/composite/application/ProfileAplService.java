package com.connectcrew.teamone.compositeservice.composite.application;

import com.connectcrew.teamone.compositeservice.composite.application.port.in.QueryProfileUseCase;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.compositeservice.composite.application.port.out.FindUserOutput;
import com.connectcrew.teamone.compositeservice.composite.domain.FullProfile;
import com.connectcrew.teamone.compositeservice.composite.domain.RepresentProject;
import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileAplService implements QueryProfileUseCase {

    private final FindProjectOutput findProjectOutput;
    private final FindUserOutput findUserOutput;

    @Override
    public Mono<FullProfile> getFullProfile(Long id) {
        return findUserOutput.getProfile(id)
                .flatMap(profile -> getRepresentProjectRes(profile.representProjects()).map(represents -> FullProfile.from(profile, represents)));

    }

    private Mono<List<RepresentProject>> getRepresentProjectRes(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> findProjectOutput.findProjectThumbnail(id)
                        .map(thumbnail -> new RepresentProject(id, FileCategory.BANNER.getUrlPath(thumbnail)))
                        .defaultIfEmpty(new RepresentProject(id, null))
                )
                .collectList();
    }
}
