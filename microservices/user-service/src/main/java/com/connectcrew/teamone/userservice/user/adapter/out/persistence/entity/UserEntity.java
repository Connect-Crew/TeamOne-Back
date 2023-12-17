package com.connectcrew.teamone.userservice.user.adapter.out.persistence.entity;

import com.connectcrew.teamone.userservice.user.domain.User;
import com.connectcrew.teamone.userservice.user.domain.enums.Role;
import com.connectcrew.teamone.userservice.user.domain.enums.Social;
import lombok.*;
import org.springframework.data.annotation.Id;
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

    private String socialId; // Social에서 사용자를 구분하는 ID

    private String username;

    private String email;

    private String role;

    private Boolean termsAgreement; // 서비스 이용 약관

    private Boolean privacyAgreement; // 개인정보 처리 방침

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public User toDomain() {
        return User.builder()
                .id(id)
                .socialId(socialId)
                .provider(Social.valueOf(provider))
                .username(username)
                .email(email)
                .role(Role.valueOf(role))
                .termsAgreement(termsAgreement)
                .privacyAgreement(privacyAgreement)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .build();
    }

    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .id(user.id())
                .socialId(user.socialId())
                .provider(user.provider().name())
                .username(user.username())
                .email(user.email())
                .role(user.role().name())
                .termsAgreement(user.termsAgreement())
                .privacyAgreement(user.privacyAgreement())
                .createdDate(user.createdDate())
                .modifiedDate(user.modifiedDate())
                .build();
    }
}
