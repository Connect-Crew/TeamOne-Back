package com.connectcrew.teamone.projectservice.member.adapter.out.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kick")
public class KickEntity {

    @Id
    private Long id;

    private Long memberId;

    private Long user;

    private String type;

    private String reason;
}
