package org.acme.domain.dtos.metrics;

public record MetricStatisticalDataDto(
    Long productId,
    String productDescription,
    Double interestRateAverage,
    Double installmentAverage,
    Double totalDesiredValue,
    Double totalCredit
) { }