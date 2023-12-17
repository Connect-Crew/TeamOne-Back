package com.connectcrew.teamone.compositeservice.file.application;

import com.connectcrew.teamone.compositeservice.file.application.port.in.DeleteFileUseCase;
import com.connectcrew.teamone.compositeservice.file.application.port.in.QueryFileUseCase;
import com.connectcrew.teamone.compositeservice.file.application.port.in.SaveFileUseCase;
import com.connectcrew.teamone.compositeservice.file.application.port.out.DeleteFileOutput;
import com.connectcrew.teamone.compositeservice.file.application.port.out.FindFileOutput;
import com.connectcrew.teamone.compositeservice.file.application.port.out.SaveFileOutput;
import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;
import com.connectcrew.teamone.compositeservice.global.exception.NotFoundException;
import com.connectcrew.teamone.compositeservice.global.exception.message.ProjectExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BannerAplService implements QueryFileUseCase, SaveFileUseCase, DeleteFileUseCase {
    private static final String UUID_PATTERNS = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";

    private final SaveFileOutput saveFileOutput;
    private final FindFileOutput findFileOutput;
    private final DeleteFileOutput deleteFileOutput;

    @Override
    public Resource find(FileCategory category, String file) {
        String[] fileNameAndExtensions = file.split("\\.");
        if (fileNameAndExtensions.length != 2) {
            throw new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_NAME.toString());
        }

        String name = fileNameAndExtensions[0];
        String extension = fileNameAndExtensions[1];

        return findFile(category, name, extension)
                .orElseThrow(() -> new NotFoundException(ProjectExceptionMessage.BANNER_NOT_FOUND.toString()));
    }

    @Override
    public MediaType findContentType(String file) {
        String[] fileNameAndExtensions = file.split("\\.");
        if (fileNameAndExtensions.length != 2) {
            throw new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_NAME.toString());
        }

        return switch (fileNameAndExtensions[1].toLowerCase()) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            default -> throw new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_EXTENSION.toString());
        };
    }

    @Override
    public Optional<Resource> findFile(FileCategory category, String name, String extension) {
        if (!Pattern.matches(UUID_PATTERNS, name)) {
            throw new IllegalArgumentException(ProjectExceptionMessage.ILLEGAL_BANNER_NAME.toString());
        }

        return findFileOutput.find(category, String.format("%s.%s", name, extension));
    }

    /**
     * 프로젝트 배너를 저장하는 함수
     * <p>
     * 이때, jpg, jpeg, png 확장자의 파일만 저장하고, 저장된 파일 이름을 반환한다.
     */
    @Override
    public Flux<String> saveBanners(FileCategory category, Flux<FilePart> banners) {
        return saveFileOutput.saveAll(category, banners);
    }

    @Override
    public Mono<Boolean> deleteBanners(FileCategory category, List<String> bannerFileNames) {
        return Flux.fromIterable(bannerFileNames)
                .map(file -> deleteFileOutput.delete(category, file))
                .all(Boolean::booleanValue);
    }


}
