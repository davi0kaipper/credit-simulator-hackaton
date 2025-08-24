package org.acme.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.acme.domain.dtos.metrics.MetricStatisticalDataDto;
import org.acme.domain.dtos.metrics.MicrometerTimer;
import org.acme.infrastructure.tables.TelemetryTable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class StatisticsRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<MetricStatisticalDataDto> getSimulationsStatistics(LocalDate referenceDate){
        return entityManager
            .createQuery(
                """
                SELECT
                    s.product.id,
                    p.name,
                    AVG(DISTINCT s.interestRate) AS interest_rate_average,
                    AVG(i.value) AS value_average,
                    SUM(DISTINCT s.desiredValue) AS total_desired_value,
                    SUM(i.value) / 2 AS total_value
                    FROM InstallmentTable i
                    JOIN SimulationResultTable sr ON i.simulationResult.id = sr.id
                    JOIN SimulationTable s ON sr.simulation.id = s.id
                    JOIN ProductTable p ON s.product.id = p.id
                    WHERE s.timestamp = :referenceDate
                    GROUP BY s.product.id, p.name
                """,
            MetricStatisticalDataDto.class
            ).setParameter("referenceDate", referenceDate)
            .getResultList();
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
