package com.connectcrew.teamone.userservice.entity;


import com.connectcrew.teamone.api.user.auth.Role;
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

    private String username;

    private String nickname;

    private String password;

    private String email;

    private Role role;

    @Column("created_date")
    private String createdDate;

    @Column("modified_date")
    private String modifiedDate;
}
