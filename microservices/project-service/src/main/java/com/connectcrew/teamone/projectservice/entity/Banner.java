package com.connectcrew.teamone.projectservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Banner {

    @Id
    private Long id;
    private Long project;
    private Integer idx;
    private String path;
}
