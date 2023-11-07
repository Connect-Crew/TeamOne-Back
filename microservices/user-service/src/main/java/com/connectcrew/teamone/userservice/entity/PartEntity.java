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
@Table(name = "part")
public class PartEntity {

    @Id
    private Long partId;

    private Long profileId;

    private String part;
}
