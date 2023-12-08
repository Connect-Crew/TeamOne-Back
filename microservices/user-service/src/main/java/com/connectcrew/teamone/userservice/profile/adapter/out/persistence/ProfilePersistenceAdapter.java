package com.connectcrew.teamone.userservice.profile.adapter.out.persistence;

import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity.PartEntity;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity.ProfileEntity;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity.RepresentProjectEntity;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository.PartRepository;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository.ProfileRepository;
import com.connectcrew.teamone.userservice.profile.adapter.out.persistence.repository.RepresentProjectRepository;
import com.connectcrew.teamone.userservice.profile.application.out.FindProfileOutput;
import com.connectcrew.teamone.userservice.profile.application.out.SaveProfileOutput;
import com.connectcrew.teamone.userservice.profile.domain.Part;
import com.connectcrew.teamone.userservice.profile.domain.Profile;
import com.connectcrew.teamone.userservice.profile.domain.RepresentProject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ProfilePersistenceAdapter implements SaveProfileOutput, FindProfileOutput {
    private final ProfileRepository profileRepository;
    private final PartRepository partRepository;
    private final RepresentProjectRepository representProjectRepository;

    @Override
    public Mono<Boolean> existsByNickname(String nickname) {
        return profileRepository.existsByNickname(nickname);
    }

    @Override
    public Mono<Profile> findByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .map(ProfileEntity::toDomain);
    }

    @Override
    public Flux<Part> findAllPartByProfileId(Long profileId) {
        return partRepository.findAllByProfileId(profileId)
                .map(PartEntity::toDomain);
    }

    @Override
    public Flux<RepresentProject> finAllRepresentProjectIdByProfileId(Long profileId) {
        return representProjectRepository.findAllByProfileId(profileId)
                .map(RepresentProjectEntity::toDomain);
    }

    @Override
    public Mono<Profile> save(Profile profile) {
        return profileRepository.save(ProfileEntity.fromDomain(profile))
                .map(ProfileEntity::toDomain);
    }
}
