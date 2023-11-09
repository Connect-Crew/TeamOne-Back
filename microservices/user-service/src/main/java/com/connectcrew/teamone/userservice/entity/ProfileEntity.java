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
@Table(name = "profile")
public class ProfileEntity {

    @Id
    private Long id;

    private Long userId;

    private String nickname;

    private String profile;

    private String introduction;

    private Double temperature;

    private Integer recvApply;

    private Integer resApply;
}
