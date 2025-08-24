package org.acme.infrastructure.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.acme.domain.dtos.metrics.MetricStatisticalDataDto;
import org.acme.domain.dtos.metrics.MicrometerTimer;
import org.acme.domain.entities.TelemetryEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class StatisticsRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Stream<MetricStatisticalDataDto> getSimulationsStatistics(LocalDate referenceDate){
        return entityManager
            .createQuery(
                """
                SELECT
                    s.productId,
                    (SELECT AVG(s.interestRate) FROM SimulationEntity s),
                    AVG(i.value),
                    (SELECT SUM(s.desiredValue) FROM SimulationEntity s),
                    (SUM(i.value) / 2.0)
                    FROM InstallmentEntity i
                    JOIN SimulationResultEntity sr ON i.simulationResult.id = sr.id
                    JOIN SimulationEntity s ON sr.simulation.id = s.id
                    WHERE s.timestamp = :referenceDate
                    GROUP BY s.productId
                """,
            MetricStatisticalDataDto.class
            ).setParameter("referenceDate", referenceDate)
            .getResultStream();
    }

    public List<TelemetryEntity> findTelemetryDataByDate(
        LocalDate referenceDate
    ){
        var telemetryData = this.entityManager.createQuery(
            """
            SELECT t FROM TelemetryEntity t
            WHERE t.referenceDate = :referenceDate
            """,
            TelemetryEntity.class
        ).setParameter("referenceDate", referenceDate)
        .getResultList();

        return telemetryData;
    }

    public Optional<TelemetryEntity> findTelemetryDataByUriMethodAndDate(
        String uri,
        String httpMethod,
        LocalDate referenceDate
    ){
        var method = httpMethod.toUpperCase();
        var telemetryData = this.entityManager.createQuery(
            """
            SELECT t FROM TelemetryEntity t
            WHERE t.method = :method
            AND t.uri = :uri
            AND t.referenceDate = :referenceDate
            """,
            TelemetryEntity.class
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
        var telemetry = new TelemetryEntity();
        telemetry.uri = uri;
        telemetry.method = method;
        telemetry.requestsAmount = (long) 1;
        telemetry.averageTime = timer == null ? 0 : timer.averageTime().intValue();
        telemetry.minTime = timer == null ? 0 : timer.minTime().intValue();
        telemetry.maxTime = timer == null ? 0 : timer.maxTime().intValue();
        telemetry.successfulRequests = TelemetryEntity.calculateSuccesfulRequest(status).longValue();
        telemetry.referenceDate = LocalDate.now();
        telemetry.persist();
    }

    public void updateWithRequest(
        TelemetryEntity telemetry,
        Integer status,
        MicrometerTimer timer
    ){
        telemetry.requestsAmount += (long) 1;
        telemetry.averageTime = timer == null ? telemetry.averageTime : timer.averageTime().intValue();
        telemetry.minTime = timer == null ? telemetry.averageTime : timer.minTime().intValue();
        telemetry.maxTime = timer == null ? telemetry.averageTime : timer.maxTime().intValue();
        telemetry.successfulRequests += TelemetryEntity.calculateSuccesfulRequest(status);
    }
}
