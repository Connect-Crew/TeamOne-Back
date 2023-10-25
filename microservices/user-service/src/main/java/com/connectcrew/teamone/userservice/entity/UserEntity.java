package com.connectcrew.teamone.userservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    private Long id;

    private String provider; // Social login 구분

    @Column("social_id")
    private String socialId; // Social에서 사용자를 구분하는 ID

    private String username;

    private String nickname;

    private String profile;

    private String email;

    private String role;

    private Boolean termsAgreement; // 서비스 이용 약관

    private Boolean privacyAgreement; // 개인정보 처리 방침

    private Boolean communityPolicyAgreement; // 커뮤니티 정책

    private Boolean adNotificationAgreement; // 광고성 정보 수신 동의

    @Column("created_date")
    private LocalDateTime createdDate;

    @Column("modified_date")
    private LocalDateTime modifiedDate;
}
