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

    private String socialId; // Social에서 사용자를 구분하는 ID

    private String username;

    private String email;

    private String role;

    private Boolean termsAgreement; // 서비스 이용 약관

    private Boolean privacyAgreement; // 개인정보 처리 방침

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
