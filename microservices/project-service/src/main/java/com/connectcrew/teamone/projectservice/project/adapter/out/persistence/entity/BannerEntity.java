package com.connectcrew.teamone.projectservice.project.adapter.out.persistence.entity;

import com.connectcrew.teamone.projectservice.project.domain.Banner;
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

    public Banner toDomain() {
        return new Banner(id, path);
    }

    public static BannerEntity from(Banner banner, Integer idx, Long projectId) {
        return BannerEntity.builder()
                .id(banner.id())
                .project(projectId)
                .idx(idx)
                .path(banner.path())
                .build();
    }

    public static List<BannerEntity> from(List<Banner> banners, Long projectId) {
        List<BannerEntity> entities = new ArrayList<>();
        for(int i = 0; i < banners.size(); i++) {
            entities.add(from(banners.get(i), i, projectId));
        }
        return entities;
    }

//    public static List<BannerEntity> from(Project project, Long projectId) {
//        List<BannerEntity> banners = new ArrayList<>();
//        for (int i = 0; i < project.banners().size(); i++) {
//            banners.add(BannerEntity.builder()
//                    .project(projectId)
//                    .idx(i)
//                    .path(project.banners().get(i))
//                    .build());
//        }
//        return banners;
//    }
}
