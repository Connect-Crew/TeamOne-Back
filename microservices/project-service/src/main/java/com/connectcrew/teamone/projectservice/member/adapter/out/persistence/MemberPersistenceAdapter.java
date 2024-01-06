package com.connectcrew.teamone.projectservice.member.adapter.out.persistence;

import com.connectcrew.teamone.api.projectservice.enums.Part;
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
import com.connectcrew.teamone.projectservice.member.domain.MemberPart;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity.PartEntity;
import com.connectcrew.teamone.projectservice.project.adapter.out.persistence.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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
                .collectMap(PartEntity::getId, p -> Part.valueOf(p.getPart()))
                .flatMapMany(idPartMap -> memberRepository.findAllByProjectId(project)
                        .flatMap(member -> memberPartRepository.findAllByMemberId(member.getId())
                                .map(memberPart -> memberPart.toDomain(idPartMap.get(memberPart.getPartId())))
                                .collectList()
                                .map(member::toDomain)
                        ));
    }

    @Override
    public Mono<Member> findByProjectAndUser(Long project, Long user) {
        return memberRepository.findByProjectIdAndUser(project, user)
                .flatMap(member -> memberPartRepository.findAllByMemberId(member.getId()).collectList()
                        .flatMap(parts -> partRepository.findAllById(parts.stream().map(MemberPartEntity::getPartId).toList())
                                .collectMap(PartEntity::getId, p -> Part.valueOf(p.getPart()))
                                .map(idPartMap -> member.toDomain(parts.stream().map(memberPart -> memberPart.toDomain(idPartMap.get(memberPart.getPartId()))).toList()))
                        )
                );
    }

    @Override
    public Flux<Apply> findAllByProjectAndUser(Long project, Long user) {
        return partRepository.findAllByProject(project)
                .collectMap(PartEntity::getId, p -> Part.valueOf(p.getPart()))
                .flatMapMany(idPartMap -> applyRepository.findAllByProjectAndUser(project, user)
                        .map(apply -> apply.toDomain(idPartMap.get(apply.getPartId())))
                );

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
    public Flux<Apply> findAllApplyByProject(Long projectId) {
        return partRepository.findAllByProject(projectId)
                .collectMap(PartEntity::getId, p -> Part.valueOf(p.getPart()))
                .flatMapMany(partMap -> applyRepository.findAllByProject(projectId)
                        .map(apply -> apply.toDomain(partMap.get(apply.getPartId()))));
    }

    @Override
    public Flux<Apply> findAllApplyByProjectAndPart(Long projectId, Part part) {
        return partRepository.findByProjectAndPart(projectId, part.name())
                .flatMapMany(partEntity -> applyRepository.findAllByProjectAndPartId(projectId, partEntity.getId()))
                .map(e -> e.toDomain(part));
    }

    @Override
    public Mono<Apply> findApplyById(Long applyId) {
        return applyRepository.findById(applyId)
                .flatMap(apply -> partRepository.findById(apply.getPartId())
                        .map(part -> apply.toDomain(Part.valueOf(part.getPart())))
                );
    }

    @Override
    public Mono<Member> save(Member member) {
        List<Long> memberPartIds = member.parts().stream()
                .map(MemberPart::id)
                .filter(Objects::nonNull)
                .toList();

        // 삭제된 part 제거
        return memberPartRepository.deleteAllByMemberIdAndIdNotIn(member.id(), memberPartIds)
                // member 저장
                .then(memberRepository.save(MemberEntity.from(member))
                        .flatMap(memberEntity -> Flux.fromIterable(member.parts())
                                .flatMap(part -> memberPartRepository.save(MemberPartEntity.from(part))
                                        .map(memberPartEntity -> memberPartEntity.toDomain(part.part())))
                                .collectList()
                                .map(memberEntity::toDomain)
                        )
                );
    }

    @Override
    public Mono<Apply> saveApply(Apply apply) {
        return applyRepository.save(ApplyEntity.from(apply))
                .map(e -> e.toDomain(apply.part()));
    }
}
