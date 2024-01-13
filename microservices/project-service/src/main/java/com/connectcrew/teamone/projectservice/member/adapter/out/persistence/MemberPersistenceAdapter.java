package com.connectcrew.teamone.projectservice.member.adapter.out.persistence;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.ApplyEntity;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.MemberEntity;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity.MemberPartEntity;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository.ApplyRepository;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository.MemberPartRepository;
import com.connectcrew.teamone.projectservice.member.adapter.out.persistence.repository.MemberRepository;
import com.connectcrew.teamone.projectservice.member.application.port.out.FindMemberOutput;
import com.connectcrew.teamone.projectservice.member.application.port.out.SaveMemberOutput;
import com.connectcrew.teamone.projectservice.member.domain.Apply;
import com.connectcrew.teamone.projectservice.member.domain.Member;
import com.connectcrew.teamone.projectservice.member.domain.ProjectMemberPart;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.PartEntity;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements FindMemberOutput, SaveMemberOutput {

    private final PartRepository partRepository;
    private final MemberRepository memberRepository;
    private final MemberPartRepository memberPartRepository;
    private final ApplyRepository applyRepository;

    @Override
    public Flux<Member> findAllByProject(Long project) {
        return partRepository.findAllByProject(project)
                .collectMap(PartEntity::getId, p -> MemberPart.valueOf(p.getPart()))
                .flatMapMany(idPartMap -> memberRepository.findAllByProjectId(project)
                        .flatMap(member -> memberPartRepository.findAllByMember(member.getId())
                                .map(memberPart -> memberPart.toDomain(idPartMap.get(memberPart.getPart())))
                                .collectList()
                                .map(member::toDomain)
                        ));
    }

    @Override
    public Mono<Member> findByProjectAndUser(Long project, Long user) {
        return memberRepository.findByProjectIdAndUser(project, user)
                .flatMap(member -> memberPartRepository.findAllByMember(member.getId()).collectList()
                        .flatMap(parts -> partRepository.findAllById(parts.stream().map(MemberPartEntity::getPart).toList())
                                .collectMap(PartEntity::getId, p -> MemberPart.valueOf(p.getPart()))
                                .map(idPartMap -> member.toDomain(parts.stream().map(memberPart -> memberPart.toDomain(idPartMap.get(memberPart.getPart()))).toList()))
                        )
                );
    }

    @Override
    public Flux<Apply> findAllByProjectAndUser(Long project, Long user) {
        return partRepository.findAllByProject(project)
                .collectMap(PartEntity::getId, p -> MemberPart.valueOf(p.getPart()))
                .flatMapMany(idPartMap -> applyRepository.findAllByProjectAndUser(project, user)
                        .map(apply -> apply.toDomain(idPartMap.get(apply.getPartId())))
                );

    }

    @Override
    public Mono<Boolean> existsMemberByPartAndUser(Long partId, Long user) {
        return memberRepository.countByPartIdAndUser(partId, user)
                .map(count -> count > 0);
    }

    @Override
    public Mono<Boolean> existsApplyByPartAndUser(Long partId, Long user) {
        return applyRepository.existsByPartIdAndUser(partId, user);
    }

    @Override
    public Flux<Apply> findAllApplyByProject(Long projectId) {
        return partRepository.findAllByProject(projectId)
                .collectMap(PartEntity::getId, p -> MemberPart.valueOf(p.getPart()))
                .flatMapMany(partMap -> applyRepository.findAllByProject(projectId)
                        .map(apply -> apply.toDomain(partMap.get(apply.getPartId()))));
    }

    @Override
    public Flux<Apply> findAllApplyByProjectAndPart(Long projectId, MemberPart part) {
        return partRepository.findByProjectAndPart(projectId, part.name())
                .flatMapMany(partEntity -> applyRepository.findAllByProjectAndPartId(projectId, partEntity.getId()))
                .map(e -> e.toDomain(part));
    }

    @Override
    public Mono<Apply> findApplyById(Long applyId) {
        return applyRepository.findById(applyId)
                .flatMap(apply -> partRepository.findById(apply.getPartId())
                        .map(part -> apply.toDomain(MemberPart.valueOf(part.getPart())))
                );
    }

    @Override
    public Mono<Member> save(Member member) {
        List<Long> memberPartIds = member.parts().stream()
                .map(ProjectMemberPart::id)
                .filter(Objects::nonNull)
                .toList();

        Map<Long, MemberPart> partMap = new HashMap<>();
        for (ProjectMemberPart part : member.parts()) {
            partMap.put(part.partId(), part.part());
        }

        // 삭제된 part 제거
        return memberPartRepository.deleteAllByMemberAndIdNotIn(member.id(), memberPartIds)
                // member 저장
                .then(memberRepository.save(MemberEntity.from(member))
                        .flatMap(memberEntity -> {
                            List<MemberPartEntity> partEntities = member.parts().stream().
                                    map(part -> MemberPartEntity.from(part, memberEntity.getId()))
                                    .toList();

                            return memberPartRepository.saveAll(partEntities)
                                    .map(memberPartEntity -> memberPartEntity.toDomain(partMap.get(memberPartEntity.getPart())))
                                    .collectList()
                                    .map(memberEntity::toDomain);
                        })
                );
    }

    @Override
    public Mono<Apply> saveApply(Apply apply) {
        return applyRepository.save(ApplyEntity.from(apply))
                .map(e -> e.toDomain(apply.part()));
    }
}
