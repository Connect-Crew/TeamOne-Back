package com.connectcrew.teamone.userservice.entity;

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
}
