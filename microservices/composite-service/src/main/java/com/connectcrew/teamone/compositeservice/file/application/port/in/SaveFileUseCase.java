package com.connectcrew.teamone.compositeservice.file.application.port.in;

import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;

public interface SaveFileUseCase {
    Flux<String> saveBanners(FileCategory category, Flux<FilePart> banners);
}
