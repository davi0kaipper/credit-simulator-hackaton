package org.acme.application.usecases;

import java.time.LocalDate;

import org.acme.domain.dtos.metrics.ApplicationMetricsDto;
import org.acme.domain.dtos.metrics.EndpointMetricsDto;
import org.acme.domain.exceptions.InvalidReferenceDate;
import org.acme.infrastructure.repository.StatisticsRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PresentApplicationMetricsUC {
    public @Inject StatisticsRepository statsRepository;

    public ApplicationMetricsDto execute(LocalDate referenceDate) throws InvalidReferenceDate {
        var endpointMetrics = this.statsRepository.findTelemetryDataByDate(referenceDate)
            .stream().map(EndpointMetricsDto::from)
            .toList();

        var applicationMetrics = new ApplicationMetricsDto(referenceDate, endpointMetrics);

        return applicationMetrics;
    }
}
