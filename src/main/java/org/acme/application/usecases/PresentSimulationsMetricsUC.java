package org.acme.application.usecases;

import java.time.LocalDate;

import org.acme.domain.dtos.summary.SimulationsSummaryByProductDto;
import org.acme.domain.dtos.summary.SimulationsSummaryDto;
import org.acme.domain.exceptions.InvalidProductId;
import org.acme.domain.exceptions.InvalidReferenceDate;
import org.acme.domain.exceptions.ProductNotFound;
import org.acme.infrastructure.repository.ProductRepository;
import org.acme.infrastructure.repository.StatisticsRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PresentSimulationsMetricsUC {
    private @Inject ProductRepository productRepository;
    private @Inject StatisticsRepository statisticsRepository;

    @Transactional
    public SimulationsSummaryDto execute(LocalDate referenceDate, Long productId)
        throws InvalidReferenceDate, InvalidProductId, ProductNotFound
    {
        if (referenceDate.isBefore(LocalDate.of(1980, 1, 1))) {
            throw new InvalidReferenceDate(
                "Informe uma data de referência válida."
            );
        }
        
        if (productId == null) {
            throw new InvalidProductId(
                "Informe um código de produto válido."
            );
        }

        if (productId < 1) {
            throw new InvalidProductId(
                "Informe um código de produto válido."
            );
        }

        var product = this.productRepository.findById(productId);
        
        if (product == null) {
            throw new ProductNotFound(
                "Nenhum produto com esse código foi encontrado."
            );
        }

        var referencedIds = this.productRepository.listIdsOfProductsReferecendBySimulations();
        var productNotReferecendBySimulations = ! referencedIds.contains(product.getId());

        if (productNotReferecendBySimulations) {
            return new SimulationsSummaryDto(
                referenceDate,
                SimulationsSummaryByProductDto.empty(product)
            );
        }

        var statistics = this.statisticsRepository.getSimulationsStatistics(product.getId(), referenceDate);
        var summary = SimulationsSummaryByProductDto.from(product, statistics.get());

        return new SimulationsSummaryDto(
            LocalDate.now(),
            summary
        );
    }
}
