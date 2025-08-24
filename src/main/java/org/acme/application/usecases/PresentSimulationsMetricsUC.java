package org.acme.application.usecases;

import java.time.LocalDate;

import org.acme.domain.dtos.summary.SimulationsSummaryByProductDto;
import org.acme.domain.dtos.summary.SimulationsSummaryDto;
import org.acme.domain.exceptions.InvalidReferenceDate;
import org.acme.infrastructure.repository.StatisticsRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PresentSimulationsMetricsUC {
    private @Inject StatisticsRepository statisticsRepository;

    @Transactional
    public SimulationsSummaryDto execute(LocalDate referenceDate) throws InvalidReferenceDate {
        if (referenceDate.isBefore(LocalDate.of(1980, 1, 1))) {
            throw new InvalidReferenceDate(
                "Informe uma data de referência válida."
            );
        }

        var statistics = this.statisticsRepository.getSimulationsStatistics(referenceDate);
        var summary = statistics.stream().map(SimulationsSummaryByProductDto::from).toList();

        return new SimulationsSummaryDto(
            referenceDate,
            summary
        );
    }
}
