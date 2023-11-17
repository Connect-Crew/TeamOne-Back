package com.connectcrew.teamone.compositeservice.service;

import org.springframework.stereotype.Service;

@Service
public class BannerService {
    public String getBannerUrlPath(String banner) {
        return banner != null ? String.format("/project/banner/%s", banner) : null;
    }
}
