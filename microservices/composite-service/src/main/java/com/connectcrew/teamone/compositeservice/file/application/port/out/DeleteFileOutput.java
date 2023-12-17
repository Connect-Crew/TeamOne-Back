package com.connectcrew.teamone.compositeservice.file.application.port.out;

import com.connectcrew.teamone.compositeservice.file.domain.enums.FileCategory;

public interface DeleteFileOutput {
    boolean delete(FileCategory category, String fileName);
}
