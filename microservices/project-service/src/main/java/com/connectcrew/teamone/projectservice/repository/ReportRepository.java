package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.projectservice.entity.Report;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReportRepository extends ReactiveCrudRepository<Report, Long> {
}
