package com.connectcrew.teamone.projectservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    @Id
    private Long id;
    private Long project;
    private Long sender;
    private String chatType;
    private String contents;
    private LocalDateTime time;
}
