package org.acme.domain.dtos;

public record MetricStatisticalDataDto(
    Double interestRateAverage,
    Double installmentAverage,
    Double totalDesiredValue,
    Double totalCredit
) { }