package com.connectcrew.teamone.compositeservice.composite.adapter.in.web.request;

import com.connectcrew.teamone.api.projectservice.enums.MemberPart;
import com.connectcrew.teamone.compositeservice.composite.application.port.in.command.CreateRecruitCommand;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecruitRequest {
    private MemberPart part;
    private String comment;
    private Long max;

    public CreateRecruitRequest(String json) {
        this.part = null;
        this.comment = null;
        this.max = null;
    }

    @JsonCreator
    public CreateRecruitRequest(@JsonProperty("part") String part, @JsonProperty("comment") String comment, @JsonProperty("max") long max) {
        this.part = MemberPart.valueOf(part);
        this.comment = comment;
        this.max = max;
    }

    public CreateRecruitCommand toCommand() {
        return new CreateRecruitCommand(part, comment, max);
    }

    @Override
    public String toString() {
        return String.format("CreateRecruitRequest(part=%s, comment=%s, max=%d)", part, comment, max);
    }
}
