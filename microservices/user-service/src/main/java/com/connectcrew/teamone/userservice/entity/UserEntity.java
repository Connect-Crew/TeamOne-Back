package com.connectcrew.teamone.userservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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

    private String nickname;

    private String email;

    private String role;

    @Column("created_date")
    private String createdDate;

    @Column("modified_date")
    private String modifiedDate;
}
