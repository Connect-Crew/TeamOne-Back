package com.connectcrew.teamone.compositeservice.file.application.port.in;

import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DeleteFileUseCase {
    Mono<Boolean> deleteBanners(FileCategory category, List<String> bannerFileNames);
}
