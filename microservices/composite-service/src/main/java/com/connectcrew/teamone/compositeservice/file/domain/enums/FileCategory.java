package com.connectcrew.teamone.compositeservice.file.domain.enums;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public enum FileCategory {
    PROFILE(Set.of("jpg", "jpeg", "png"), "NOT IMPLEMENTED"),
    BANNER(Set.of("jpg", "jpeg", "png"), "/project/banner"),
    ;
    private final Set<String> extensions;
    private final String baseUri;

    public boolean isAllowedExtension(String extension) {
        return extensions.contains(extension);
    }

    public String getUrlPath(String file) {
        if(file == null) return null;
        return String.format("%s/%s", baseUri, file);
    }
}
