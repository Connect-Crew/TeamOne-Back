package com.connectcrew.teamone.compositeservice.file.application.port.out;

import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;

public interface SaveFileOutput {
    Flux<String> saveAll(FileCategory category, Flux<FilePart> files);
}
