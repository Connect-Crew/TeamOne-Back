package com.connectcrew.teamone.compositeservice.service;

import com.connectcrew.teamone.api.exception.message.ProjectExceptionMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class BannerService {
    private static final String UUID_PATTERNS = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
    private final String BANNER_PATH;

    public BannerService(@Value("${resource.banner}") String bannerPath) {
        this.BANNER_PATH = bannerPath;
    }

    public String getBannerUrlPath(String banner) {
        return banner != null ? String.format("/project/banner/%s", banner) : null;
    }

    public Optional<MediaType> getMediaType(String extension) {
        return switch (extension) {
            case "jpg", "jpeg" -> Optional.of(MediaType.IMAGE_JPEG);
            case "png" -> Optional.of(MediaType.IMAGE_PNG);
            default -> Optional.empty();
        };
    }

    public Resource getBanner(String name, String extension) {
        try {
            if (!Pattern.matches(UUID_PATTERNS, name)) {
                throw new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_NAME.toString());
            }

            // 이미지 파일 경로 설정
            Path imagePath = Paths.get(BANNER_PATH).resolve(String.format("%s.%s", name, extension));
            Resource resource = new UrlResource(imagePath.toUri());

            // 이미지 응답 생성
            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalArgumentException(ProjectExceptionMessage.BANNER_NOT_FOUND.toString());
            }

            return resource;
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(ProjectExceptionMessage.BANNER_NOT_FOUND.toString(), ex);
        }

    }

    /**
     * 프로젝트 배너를 저장하는 함수
     * <p>
     * 이때, jpg, jpeg, png 확장자의 파일만 저장하고, 저장된 파일 이름을 반환한다.
     */
    public Flux<String> saveBanners(Flux<FilePart> banner) {
        return banner
                .filter(file -> List.of(".jpg", ".jpeg", ".png").contains(getFileExtension(file.filename())))
                .flatMap(file -> {
                    String uuidFileName = UUID.randomUUID() + getFileExtension(file.filename());
                    Path filePath = Path.of(BANNER_PATH + "/" + uuidFileName);
                    return file.transferTo(filePath).thenReturn(uuidFileName);
                });
    }

    public Mono<Boolean> deleteBanners(List<String> bannerFileNames) {
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
}
