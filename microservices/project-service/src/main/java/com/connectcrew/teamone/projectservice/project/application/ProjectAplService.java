package com.connectcrew.teamone.projectservice.project.application;

import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.projectservice.member.application.port.out.FindMemberOutput;
import com.connectcrew.teamone.projectservice.member.application.port.out.SaveMemberOutput;
import com.connectcrew.teamone.projectservice.project.application.port.in.CreateProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.QueryProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.UpdateProjectUseCase;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.CreateProjectCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.CreateRecruitCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.FavoriteCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.command.ReportCommand;
import com.connectcrew.teamone.projectservice.project.application.port.in.query.ProjectQuery;
import com.connectcrew.teamone.projectservice.project.application.port.out.FindProjectOutput;
import com.connectcrew.teamone.projectservice.project.application.port.out.SaveProjectOutput;
import com.connectcrew.teamone.projectservice.project.application.port.out.SendReportMessageOutput;
import com.connectcrew.teamone.projectservice.project.application.port.out.UpdateProjectOutput;
import com.connectcrew.teamone.projectservice.project.domain.Project;
import com.connectcrew.teamone.projectservice.project.domain.RecruitStatus;
import com.connectcrew.teamone.projectservice.project.domain.vo.ProjectItem;
import com.connectcrew.teamone.projectservice.project.domain.vo.UserRelationWithProject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectAplService implements QueryProjectUseCase, CreateProjectUseCase, UpdateProjectUseCase {
    private static final String UUID_PATTERNS = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
    private final FindProjectOutput findProjectOutput;
    private final SaveProjectOutput saveProjectOutput;
    private final UpdateProjectOutput updateProjectOutput;
    private final FindMemberOutput findMemberOutput;
    private final SaveMemberOutput saveMemberOutput;
    private final SendReportMessageOutput sendReportMessageOutput;

    @Override
    public Flux<ProjectItem> findAllByQuery(ProjectQuery query) {
        log.trace("findAllByQuery - query: {}", query);
        return findProjectOutput.findAllByQuery(query.toOption());
    }

    @Override
    public Flux<ProjectItem> findAllByUserId(Long userId) {
        log.trace("findAllByUserId - userId: {}", userId);
        return findProjectOutput.findAllByUserId(userId);
    }

    @Override
    public Mono<Tuple2<Project, UserRelationWithProject>> findById(Long id, Long userId) {
        log.trace("findById - id: {}, userId: {}", id, userId);
        return findProjectOutput.findById(id)
                .flatMap(project -> getUserRelationWithProject(id, userId)
                         .map(relation -> Tuples.of(project, relation)));
    }

    @Override
    public Mono<String> findProjectThumbnail(Long id) {
        return findProjectOutput.findProjectThumbnail(id);
    }

    @NotNull
    private Mono<UserRelationWithProject> getUserRelationWithProject(Long id, Long userId) {
        return findMemberOutput.findAllUserPartByProjectAndUser(id, userId)
                .collect(Collectors.toSet())
                .map(UserRelationWithProject::from);
    }

    @Override
    @Transactional
    public Mono<Long> create(CreateProjectCommand command) {
        log.trace("create - command: {}", command);
        return validateCommand(command)
                .then(saveProjectOutput.create(command.toDomain()))
                .flatMap(project -> saveMemberOutput.saveMember(command.leader(), getPartIds(project.recruitStatuses(), command.leaderParts())).thenReturn(project.id()));
    }

    private Mono<CreateProjectCommand> validateCommand(CreateProjectCommand command) {
        // 1. title은 2글자 이상 30글자 이하
        if (command.title().length() < 2)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.TITLE_LENGTH_2_OVER.toString()));
        if (command.title().length() > 30)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.TITLE_LENGTH_30_UNDER.toString()));

        // 2. banner는 최대 3개. 경로, 이름, 확장자가 유효한지 검사
        if (command.banners() != null && command.banners().size() > 3)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.BANNER_MAX_3.toString()));

        if (command.banners() != null) {
            for (String banner : command.banners()) {
                String[] fileNameAndExtensions = banner.split("\\.");
                String filename = fileNameAndExtensions[0];
                String extension = fileNameAndExtensions[1];
                if (!Pattern.matches(UUID_PATTERNS, filename)) {
                    return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_NAME.toString()));
                }

                if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("jpeg")) {
                    return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_EXTENSION.toString()));
                }
            }
        }

        // 4. careerMin은 careerMax보다 이전 값이어야 함.
        if (command.careerMin().getId() > command.careerMax().getId())
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.CAREER_MIN_BEFORE_MAX.toString()));

        // 5. chatRoomId는 UUID
        if (!Pattern.matches(UUID_PATTERNS, command.chatRoomId()))
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_CHATROOM_ID.toString()));

        // 6. category는 최소 1개 최대 3개
        if (command.category().size() < 1)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.CATEGORY_MIN_1.toString()));

        if (command.category().size() > 3)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.CATEGORY_MAX_3.toString()));

        // 7. introduction은 1000글자 이하
        if (command.introduction().length() > 1000)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.INTRODUCTION_LENGTH_1000_UNDER.toString()));

        // 8. recruit 조건 검사 (comment는 최대 30글자, max는 0 이상인지, 모든 recruit의 max의 합이 10 이하인지)
        int recruitMaxSum = 0;
        for (CreateRecruitCommand recruit : command.recruits()) {
            if (recruit.comment().length() > 30)
                return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.RECRUIT_COMMENT_LENGTH_30_UNDER.toString()));

            if (recruit.max() < 0)
                return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.RECRUIT_MAX_0_OVER.toString()));

            recruitMaxSum += recruit.max();
        }
        if (recruitMaxSum > 30)
            return Mono.error(new IllegalArgumentException(ProjectExceptionMessage.RECRUIT_MAX_SUM_30_UNDER.toString()));

        return Mono.just(command);
    }

    private List<Long> getPartIds(List<RecruitStatus> recruits, List<MemberPart> parts) {
        return recruits.stream()
                .filter(recruit -> parts.contains(recruit.part()))
                .map(RecruitStatus::id)
                .toList();
    }

    @Override
    public Mono<Boolean> report(ReportCommand command) {
        return saveProjectOutput.report(command.toDomain())
                .doOnNext(sendReportMessageOutput::send)
                .thenReturn(true);
    }

    @Override
    public Mono<Integer> update(FavoriteCommand command) {
        return updateProjectOutput.updateFavorite(command.project(), command.favorite() ? 1 : -1);

    }
}
