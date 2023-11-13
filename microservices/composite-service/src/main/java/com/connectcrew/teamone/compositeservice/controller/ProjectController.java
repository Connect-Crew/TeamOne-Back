package com.connectcrew.teamone.compositeservice.controller;

import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import com.connectcrew.teamone.api.project.*;
import com.connectcrew.teamone.api.user.favorite.FavoriteType;
import com.connectcrew.teamone.api.user.profile.Profile;
import com.connectcrew.teamone.compositeservice.auth.JwtProvider;
import com.connectcrew.teamone.compositeservice.param.ApplyParam;
import com.connectcrew.teamone.compositeservice.param.ProjectFavoriteParam;
import com.connectcrew.teamone.compositeservice.param.ProjectInputParam;
import com.connectcrew.teamone.compositeservice.param.ReportParam;
import com.connectcrew.teamone.compositeservice.request.FavoriteRequest;
import com.connectcrew.teamone.compositeservice.request.ProjectRequest;
import com.connectcrew.teamone.compositeservice.request.UserRequest;
import com.connectcrew.teamone.compositeservice.resposne.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController {
    private static final String UUID_PATTERNS = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
    private final String BANNER_PATH;
    private final JwtProvider jwtProvider;
    private final UserRequest userRequest;
    private final ProjectRequest projectRequest;

    private final FavoriteRequest favoriteRequest;

    private final ProjectBasicInfo projectBasicInfo;

    public ProjectController(JwtProvider provider, UserRequest userRequest, ProjectRequest projectRequest, FavoriteRequest favoriteRequest, @Value("${resource.banner}") String bannerPath) {
        this.jwtProvider = provider;
        this.userRequest = userRequest;
        this.projectRequest = projectRequest;
        this.favoriteRequest = favoriteRequest;
        this.BANNER_PATH = bannerPath;
        this.projectBasicInfo = new ProjectBasicInfo();
    }

    @GetMapping("/")
    public ProjectBasicInfo getProjectBasicInfo() {
        return projectBasicInfo;
    }

    @GetMapping("/banner/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) throws MalformedURLException {
        String[] fileNameAndExtensions = filename.split("\\.");
        String name = fileNameAndExtensions[0];
        String extension = fileNameAndExtensions[1];

        if (!Pattern.matches(UUID_PATTERNS, name)) {
            throw new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_NAME.toString());
        }

        MediaType mediaType = switch (extension) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            default -> null;
        };

        if (mediaType == null) {
            throw new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_EXTENSION.toString());
        }

        // 이미지 파일 경로 설정
        Path imagePath = Paths.get(BANNER_PATH).resolve(String.format("%s.%s", name, extension));
        Resource resource = new UrlResource(imagePath.toUri());

        // 이미지 응답 생성
        if (!resource.exists() || !resource.isReadable()) {
            throw new IllegalArgumentException(ProjectExceptionMessage.BANNER_NOT_FOUND.toString());
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }


    @GetMapping("/list")
    private Mono<List<ProjectItemRes>> getProjectList(@RequestHeader(JwtProvider.AUTH_HEADER) String token, ProjectFilterOption option) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        log.trace("getProjectList: {}", option);
        return projectRequest.getProjectList(option)
                .collectList()
                .flatMap(projects -> {
                    if (projects.size() == 0) {
                        return Mono.just(Tuples.of(projects, new HashMap<Long, Boolean>()));
                    }
                    List<Long> profileIds = projects.stream().map(ProjectItem::id).toList();

                    return favoriteRequest.isFavorite(id, FavoriteType.PROJECT, profileIds)
                            .map(favoriteMap -> Tuples.of(projects, favoriteMap));
                })
                .map(tuple -> {
                    Map<Long, Boolean> favoriteMap = tuple.getT2();
                    return tuple.getT1().stream()
                            .map(project -> {
                                Boolean isFavorite = favoriteMap.getOrDefault(project.id(), false);
                                String thumbnail = project.thumbnail() != null ? String.format("/project/banner/%s", project.thumbnail()) : null;
                                return new ProjectItemRes(project, isFavorite, thumbnail);
                            })
                            .toList();
                });
    }

    @GetMapping("/{projectId}")
    private Mono<ProjectDetailRes> getProjectDetail(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @PathVariable Long projectId) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return projectRequest.getProjectDetail(projectId)
                .flatMap(project -> {
                    Set<Long> profileIds = new HashSet<>();
                    profileIds.add(project.leader());
                    profileIds.addAll(project.members().stream().map(ProjectMember::memberId).toList());

                    return Flux.fromIterable(profileIds)
                            .flatMap(userRequest::getProfile)
                            .collectMap(Profile::id, p -> p)
                            .map(profileMap -> Tuples.of(project, profileMap));
                })
                .flatMap(tuple -> {
                    ProjectDetail project = tuple.getT1();
                    Map<Long, Profile> profileMap = tuple.getT2();

                    return favoriteRequest.isFavorite(id, FavoriteType.PROJECT, projectId)
                            .map(favorite -> Tuples.of(project, favorite, profileMap));
                })
                .map(tuple -> {
                    List<String> banners = tuple.getT1().banners().stream().map(banner -> String.format("/project/banner/%s", banner)).toList();
                    return new ProjectDetailRes(tuple.getT1(), banners, tuple.getT2(), tuple.getT3());
                });
    }

    @PostMapping("/")
    private Mono<LongValueRes> createProject(
            @RequestHeader(JwtProvider.AUTH_HEADER) String token,
            @RequestPart("banner") Flux<FilePart> banner,
            @RequestPart("param") ProjectInputParam param
    ) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return saveBanners(banner)
                .collectList()
                .flatMap(paths -> {
                    ProjectInput input = new ProjectInput(
                            param.title(),
                            paths,
                            param.region(),
                            param.online(),
                            param.state(),
                            param.careerMin(),
                            param.careerMax(),
                            id,
                            param.leaderParts(),
                            param.category(),
                            param.goal(),
                            param.introduction(),
                            param.recruits(),
                            param.skills()
                    );

                    return projectRequest.saveProject(input)
                            .onErrorResume(ex -> deleteBanners(paths).then(Mono.error(ex))); // 프로젝트 글 작성 실패시 저장된 배너 삭제
                })
                .map(LongValueRes::new);
    }

    /**
     * 프로젝트 배너를 저장하는 함수
     * <p>
     * 이때, jpg, jpeg, png 확장자의 파일만 저장하고, 저장된 파일 이름을 반환한다.
     */
    private Flux<String> saveBanners(Flux<FilePart> banner) {
        return banner
                .filter(file -> List.of(".jpg", ".jpeg", ".png").contains(getFileExtension(file.filename())))
                .flatMap(file -> {
                    String uuidFileName = UUID.randomUUID() + getFileExtension(file.filename());
                    Path filePath = Path.of(BANNER_PATH + "/" + uuidFileName);
                    return file.transferTo(filePath).thenReturn(uuidFileName);
                });
    }

    private Mono<Boolean> deleteBanners(List<String> bannerFileNames) {
        return Flux.fromIterable(bannerFileNames)
                .flatMap(fileName -> {
                    Path filePath = Path.of(BANNER_PATH + "/" + fileName);
                    if (filePath.toFile().exists())
                        return Mono.just(filePath.toFile().delete());
                    else
                        return Mono.just(true);
                })
                .all(Boolean::booleanValue);
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex != -1) ? fileName.substring(lastDotIndex) : "";
    }


    @PostMapping("/favorite")
    private Mono<FavoriteRes> favoriteProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ProjectFavoriteParam param) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return favoriteRequest.setFavorite(id, FavoriteType.PROJECT, param.projectId())
                .flatMap(newFavorite -> projectRequest.updateFavorite(new FavoriteUpdateInput(param.projectId(), newFavorite ? 1 : -1))
                        .map(favorite -> new FavoriteRes(param.projectId(), newFavorite, favorite))
                );
    }

    @PostMapping("/apply")
    private Mono<BooleanValueRes> applyProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ApplyParam param) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return projectRequest.applyProject(param.toInput(id)).map(BooleanValueRes::new);
    }

    @PostMapping("/report")
    private Mono<BooleanValueRes> reportProject(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody ReportParam param) {
        String removedPrefix = token.replace(JwtProvider.BEARER_PREFIX, "");
        Long id = jwtProvider.getId(removedPrefix);

        return projectRequest.reportProject(param.toInput(id)).map(BooleanValueRes::new);
    }
}