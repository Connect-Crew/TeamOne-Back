package com.connectcrew.teamone.userservice.profile.adapter.out.persistence.entity;

import com.connectcrew.teamone.userservice.profile.domain.Profile;
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

    public Profile toDomain() {
        return Profile.builder()
                .id(id)
                .userId(userId)
                .nickname(nickname)
                .profile(profile)
                .introduction(introduction)
                .temperature(temperature)
                .recvApply(recvApply)
                .resApply(resApply)
                .build();
    }

    public static ProfileEntity fromDomain(Profile profile) {
        return ProfileEntity.builder()
                .id(profile.id())
                .userId(profile.userId())
                .nickname(profile.nickname())
                .profile(profile.profile())
                .introduction(profile.introduction())
                .temperature(profile.temperature())
                .recvApply(profile.recvApply())
                .resApply(profile.resApply())
                .build();
    }
}
