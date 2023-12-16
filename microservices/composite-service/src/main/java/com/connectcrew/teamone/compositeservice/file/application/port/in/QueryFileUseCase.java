package com.connectcrew.teamone.compositeservice.file.application.port.in;

import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.util.Optional;

public interface QueryFileUseCase {

    Resource find(FileCategory category, String file);
    MediaType findContentType(String file);

    Optional<Resource> findFile(FileCategory category, String name, String extension);
}
