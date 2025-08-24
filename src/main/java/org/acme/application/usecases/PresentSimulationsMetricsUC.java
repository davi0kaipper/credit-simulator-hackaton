package org.acme.application.usecases;

import java.time.LocalDate;
import java.util.List;

import org.acme.domain.dtos.summary.SimulationsSummaryByProductDto;
import org.acme.domain.dtos.summary.SimulationsSummaryDto;
import org.acme.domain.exceptions.InvalidReferenceDate;
import org.acme.infrastructure.repository.StatisticsRepository;
import org.acme.infrastructure.tables.ProductTable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PresentSimulationsMetricsUC {
    private @Inject StatisticsRepository statisticsRepository;
    private LocalDate referenceDate;

    public SimulationsSummaryDto execute(LocalDate referenceDate) throws InvalidReferenceDate {
        if (referenceDate.isBefore(LocalDate.of(1980, 1, 1))) {
            throw new InvalidReferenceDate(
                "Informe uma data de referência válida."
            );
        }

        this.referenceDate = referenceDate;
        List<ProductTable> products = this.getProducts();
        var summary = this.getSummaries(products);
        return new SimulationsSummaryDto(
            referenceDate,
            summary
        );
    }

    @Transactional
    protected List<ProductTable> getProducts() {
        return ProductTable.listAll();
    }

    @Transactional
    protected List<SimulationsSummaryByProductDto> getSummaries(List<ProductTable> products) {
        var summary = this.statisticsRepository.getSimulationsStatistics(this.referenceDate)
            .map(statistic -> {
                statistic.setProductDescription(
                    products.stream().filter(product -> product.getId().equals(statistic.getProductId()))
                    .toList().getFirst().getName()
                );
                var instance = SimulationsSummaryByProductDto.from(statistic);
                return instance;
            }).toList();
        
        return summary;
    }
}
