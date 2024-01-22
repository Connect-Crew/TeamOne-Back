package com.connectcrew.teamone.projectservice.project.application.port.out;

import com.connectcrew.teamone.projectservice.project.domain.Report;

public interface SendReportMessageOutput {
    void send(Report report);
}
