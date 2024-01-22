package com.connectcrew.teamone.projectservice.project.adapter.out.event;

import com.connectcrew.teamone.api.constants.KafkaEventTopic;
import com.connectcrew.teamone.projectservice.project.application.port.out.SendReportMessageOutput;
import com.connectcrew.teamone.projectservice.project.domain.Report;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReportEventAdapter implements SendReportMessageOutput {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(Report report) {
        log.info("send report: {}", report);
        try {
            String body = objectMapper.writeValueAsString(report.toNotification());
            kafkaTemplate.send(KafkaEventTopic.ReportNotification, body);
        } catch (Exception e) {
            log.error("send error - topic: {}, body: {}", KafkaEventTopic.ReportNotification, report, e);
        }
    }
}
