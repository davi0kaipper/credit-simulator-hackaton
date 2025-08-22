package org.acme.infrastructure.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.acme.domain.dtos.MetricStatisticalDataDto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class StatisticsRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<MetricStatisticalDataDto> getSimulationsStatistics(
        Long productId,
        LocalDate referenceDate
    ){
        return entityManager
            .createQuery(
                """
                SELECT
                    (SELECT AVG(s.interestRate) FROM SimulationTable s
                    WHERE s.timestamp = :referenceDate
                    AND s.product.id = :product_id) AS interest_rate_average,

                    (SELECT AVG(i.value) FROM InstallmentTable i
                    WHERE i.simulationResult.simulation.timestamp = :referenceDate
                    AND i.simulationResult.simulation.product.id = :product_id) AS value_average,

                    (SELECT SUM(s.desiredValue) FROM SimulationTable s
                    WHERE s.timestamp = :referenceDate
                    AND s.product.id = :product_id) AS total_desired_value,

                    (SELECT SUM(i.value) FROM InstallmentTable i
                    WHERE i.simulationResult.simulation.timestamp = :referenceDate
                    AND i.simulationResult.simulation.product.id = :product_id) AS total_value
                """,
            MetricStatisticalDataDto.class
            ).setParameter("product_id", productId)
            .setParameter("referenceDate", referenceDate)
            .getResultList()
            .stream()
            .findFirst();
    }
}
