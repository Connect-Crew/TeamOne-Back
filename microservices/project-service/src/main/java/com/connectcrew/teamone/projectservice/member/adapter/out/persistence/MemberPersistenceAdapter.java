package com.connectcrew.teamone.projectservice.member.adapter.out.persistence;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.ApplyEntity;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.MemberEntity;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository.ApplyRepository;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository.MemberRepository;
import com.connectcrew.teamone.projectservice.member.application.port.out.FindMemberOutput;
import com.connectcrew.teamone.projectservice.member.application.port.out.SaveMemberOutput;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.member.domain.MemberState;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.PartEntity;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements FindMemberOutput, SaveMemberOutput {

    private final PartRepository partRepository;
    private final MemberRepository memberRepository;
    private final ApplyRepository applyRepository;

    @Override
    public Mono<List<Member>> findAllByProject(Long project) {
        Mono<Map<Long, MemberPart>> parts = partRepository.findAllByProject(project)
                .collectMap(PartEntity::getId, p -> MemberPart.valueOf(p.getPart()));
        Mono<List<MemberEntity>> members = memberRepository.findAllByProject(project).collectList();

        return Mono.zip(parts, members)
                .map(tuple -> {
                    Map<Long, MemberPart> partMap = tuple.getT1();
                    Map<Long, MemberEntity> memberMap = tuple.getT2().stream().collect(Collectors.toMap(MemberEntity::getId, m -> m));

                    return memberMap.values().stream()
                            .collect(Collectors.groupingBy(MemberEntity::getUser, Collectors.mapping(m -> partMap.get(m.getPartId()), Collectors.toList())))
                            .entrySet().stream().map(e -> new Member(e.getKey(), false, e.getValue(), memberMap.get(e.getKey()).getState())).toList();
                });
    }

    @Override
    public Flux<MemberPart> findAllUserPartByProjectAndUser(Long project, Long user) {
        return Flux.merge(
                    partRepository.findAllUserPartByProjectAndUser(project, user),
                    partRepository.findAllAppliedPartByProjectAndUser(project, user)
                )
                .map(part -> MemberPart.valueOf(part.getPart()));
    }

    @Override
    public Mono<Boolean> existsMemberByPartAndUser(Long partId, Long user) {
        return memberRepository.existsByPartIdAndUser(partId, user);
    }

    @Override
    public Mono<Boolean> existsApplyByPartAndUser(Long partId, Long user) {
        return applyRepository.existsByPartIdAndUser(partId, user);
    }

    @Override
    public Flux<Apply> findAllAppliesByProject(Long projectId) {
        return partRepository.findAllByProject(projectId)
                .collectMap(PartEntity::getId, p -> MemberPart.valueOf(p.getPart()))
                .flatMapMany(partMap -> applyRepository.findAllByProject(projectId)
                        .map(apply -> apply.toDomain(partMap.get(apply.getPartId()))));
    }

    @Override
    public Flux<Apply> findAllApplies(Long projectId, MemberPart part) {
        return partRepository.findByProjectAndPart(projectId, part.name())
                .flatMapMany(partEntity -> applyRepository.findAllByProjectAndPartId(projectId, partEntity.getId()))
                .map(e -> e.toDomain(part));
    }

    @Override
    public Mono<Long> saveMember(Long userId, List<Long> parts) {
        List<MemberEntity> members = parts.stream().map(partId -> MemberEntity
                        .builder()
                        .partId(partId)
                        .user(userId)
                        .state(MemberState.ACTIVE)
                        .build())
                .toList();
        return memberRepository.saveAll(members)
                .collectList()
                .thenReturn(userId);
    }

    @Override
    public Mono<Apply> saveApply(Apply apply) {
        return applyRepository.save(ApplyEntity.from(apply))
                .map(e -> e.toDomain(apply.part()));
    }
}
