package com.connectcrew.teamone.compositeservice.file.application.port.out;

import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;
import org.springframework.core.io.Resource;

import java.util.Optional;

public interface FindFileOutput {

    Optional<Resource> find(FileCategory category, String fileName);
}
