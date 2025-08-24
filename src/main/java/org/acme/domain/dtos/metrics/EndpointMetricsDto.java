package org.acme.domain.dtos.metrics;

import org.acme.domain.entities.TelemetryEntity;

public record EndpointMetricsDto(
    String uri,
    Long requestsAmount,
    Integer averageTime,
    Integer minTime,
    Integer maxTime,
    Double successPercentage
){
    public static EndpointMetricsDto from(TelemetryEntity telemetry) {
        Double successPercentage = telemetry.successfulRequests.doubleValue() / telemetry.requestsAmount.doubleValue();
        return new EndpointMetricsDto(
            telemetry.method + " " + telemetry.uri,
            telemetry.requestsAmount,
            telemetry.averageTime,
            telemetry.minTime,
            telemetry.maxTime,
            successPercentage
        );
    }
}
