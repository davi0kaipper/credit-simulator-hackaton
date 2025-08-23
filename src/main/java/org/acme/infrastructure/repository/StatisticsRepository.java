package org.acme.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.acme.domain.dtos.MetricStatisticalDataDto;
import org.acme.domain.dtos.metrics.MicrometerTimer;
import org.acme.infrastructure.tables.TelemetryTable;

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

    public List<TelemetryTable> findTelemetryDataByDate(
        LocalDate referenceDate
    ){
        var telemetryData = this.entityManager.createQuery(
            """
            SELECT t FROM TelemetryTable t
            WHERE t.referenceDate = :referenceDate
            """,
            TelemetryTable.class
        ).setParameter("referenceDate", referenceDate)
        .getResultList();

        return telemetryData;
    }

    public Optional<TelemetryTable> findTelemetryDataByUriMethodAndDate(
        String uri,
        String httpMethod,
        LocalDate referenceDate
    ){
        var method = httpMethod.toUpperCase();
        var telemetryData = this.entityManager.createQuery(
            """
            SELECT t FROM TelemetryTable t
            WHERE t.method = :method
            AND t.uri = :uri
            AND t.referenceDate = :referenceDate
            """,
            TelemetryTable.class
        ).setParameter("uri", uri)
        .setParameter("method", method)
        .setParameter("referenceDate", referenceDate)
        .getSingleResultOrNull();

        return Optional.ofNullable(telemetryData);
    }

    public void createNewTelemetry(
        String method,
        String uri,
        Integer status,
        MicrometerTimer timer
    ){
        var telemetry = new TelemetryTable();
        telemetry.uri = uri;
        telemetry.method = method;
        telemetry.requestsAmount = (long) 1;
        telemetry.averageTime = timer == null ? 0 : timer.averageTime().intValue();
        telemetry.minTime = timer == null ? 0 : timer.minTime().intValue();
        telemetry.maxTime = timer == null ? 0 : timer.maxTime().intValue();
        telemetry.successfulRequests = TelemetryTable.calculateSuccesfulRequest(status).longValue();
        telemetry.referenceDate = LocalDate.now();
        telemetry.persist();
    }

    public void updateWithRequest(
        TelemetryTable telemetry,
        Integer status,
        MicrometerTimer timer
    ){
        telemetry.requestsAmount += (long) 1;
        telemetry.averageTime = timer == null ? telemetry.averageTime : timer.averageTime().intValue();
        telemetry.minTime = timer == null ? telemetry.averageTime : timer.minTime().intValue();
        telemetry.maxTime = timer == null ? telemetry.averageTime : timer.maxTime().intValue();
        telemetry.successfulRequests += TelemetryTable.calculateSuccesfulRequest(status);
    }
}
