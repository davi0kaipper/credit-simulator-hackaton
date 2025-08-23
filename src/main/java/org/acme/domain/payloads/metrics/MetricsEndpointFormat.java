package org.acme.domain.payloads.metrics;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.acme.domain.dtos.metrics.EndpointMetricsDto;

public record MetricsEndpointFormat(
    String nomeApi,
    Long qtdRequisicoes,
    Integer tempoMedio,
    Integer tempoMinimo,
    Integer tempoMaximo,
    BigDecimal percentualSucesso
) {
    public static MetricsEndpointFormat from(EndpointMetricsDto metricsEndpointDto) {
        return new MetricsEndpointFormat(
            metricsEndpointDto.uri(),
            metricsEndpointDto.requestsAmount(),
            metricsEndpointDto.averageTime(),
            metricsEndpointDto.minTime(),
            metricsEndpointDto.maxTime(),
            BigDecimal.valueOf(metricsEndpointDto.successPercentage()).setScale(2, RoundingMode.HALF_UP)
        );
    }
}
