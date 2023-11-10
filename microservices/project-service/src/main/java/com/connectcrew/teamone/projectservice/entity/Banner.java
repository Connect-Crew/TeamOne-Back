package com.connectcrew.teamone.projectservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Banner {

    @Id
    private Long id;
    private Long project;
    private Integer idx;
    private String path;

    public Banner(){
        id = null;
        project = null;
        idx = null;
        path = null;
    }
}
