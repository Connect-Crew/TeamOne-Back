package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity;

import com.connectcrew.teamone.projectservice.project.domain.Project;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "banner")
public class BannerEntity {

    @Id
    private Long id;
    private Long project;
    private Integer idx;
    private String path;

    public BannerEntity(){
        id = null;
        project = null;
        idx = null;
        path = null;
    }

    public static List<BannerEntity> from(Project project, Long projectId) {
        List<BannerEntity> banners = new ArrayList<>();
        for (int i = 0; i < project.banners().size(); i++) {
            banners.add(BannerEntity.builder()
                    .project(projectId)
                    .idx(i)
                    .path(project.banners().get(i))
                    .build());
        }
        return banners;
    }
}
