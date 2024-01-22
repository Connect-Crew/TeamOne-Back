package com.connectcrew.teamone.compositeservice.file.adapter.out.file;

import com.connectcrew.teamone.compositeservice.file.application.port.out.DeleteFileOutput;
import com.connectcrew.teamone.compositeservice.file.application.port.out.FindFileOutput;
import com.connectcrew.teamone.compositeservice.file.application.port.out.SaveFileOutput;
import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StaticFileAdapter implements SaveFileOutput, FindFileOutput, DeleteFileOutput {

    @Value("${resource.base-path}")
    private String basePath;

    @Override
    public Flux<String> saveAll(FileCategory category, Flux<FilePart> files) {
        return files
                .doOnNext(file -> log.trace("try save file: {}, extension: {}", file.filename(), getFileExtension(file.filename())))
                .filter(file -> category.isAllowedExtension(getFileExtension(file.filename())))
                .flatMap(file -> {
                    String fileName = String.format("%s.%s", UUID.randomUUID(), getFileExtension(file.filename()));
                    Path filePath = Path.of(basePath, category.name().toLowerCase(), fileName);
                    log.trace("save file: {}", filePath);
                    return file.transferTo(filePath).thenReturn(fileName);
                });
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다.");
        }

//        return (lastDotIndex != -1) ? fileName.substring(lastDotIndex) : "";
        return fileName.substring(lastDotIndex + 1);
    }

    @Override
    public Optional<Resource> find(FileCategory category, String fileName) {
        log.trace("Try to load file: {}", fileName);
        try {
            Path filePath = Path.of(basePath, category.name().toLowerCase(), fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                log.warn("File not found: {}", filePath);
                return Optional.empty();
            }

            log.trace("File loaded: {}", filePath);
            return Optional.of(resource);
        } catch (MalformedURLException e) {
            log.warn("Load file failed: {}", fileName, e);
            return Optional.empty();
        }
    }


    @Override
    public boolean delete(FileCategory category, String fileName) {
        log.trace("Try to delete file: {}", fileName);
        Path filePath = Path.of(basePath, category.name(), fileName);
        if (filePath.toFile().exists()) {
            log.trace("File deleted: {}", filePath);
            return filePath.toFile().delete();
        }
        log.warn("File not found: {}", filePath);
        return false;
    }
}
