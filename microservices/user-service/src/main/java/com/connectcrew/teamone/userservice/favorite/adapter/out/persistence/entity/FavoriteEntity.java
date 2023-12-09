package com.connectcrew.teamone.userservice.favorite.adapter.out.persistence.entity;

import com.connectcrew.teamone.userservice.favorite.domain.Favorite;
import com.connectcrew.teamone.userservice.favorite.domain.enums.FavoriteType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorite")
public class FavoriteEntity {

    @Id
    private Long favoriteId;

    private Long profileId;

    private String type;

    private Long target;

    public static FavoriteEntity fromDomain(Favorite favorite) {
        return FavoriteEntity.builder()
                .favoriteId(favorite.favoriteId())
                .profileId(favorite.userId())
                .type(favorite.favoriteType().name())
                .target(favorite.target())
                .build();
    }

    public Favorite toDomain() {
        return new Favorite(
                favoriteId,
                profileId,
                FavoriteType.valueOf(type),
                target
        );
    }
}
