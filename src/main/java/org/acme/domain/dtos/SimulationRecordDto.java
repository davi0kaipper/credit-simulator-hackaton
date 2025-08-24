package org.acme.domain.dtos;

public record SimulationRecordDto(
    Long simulationId,
    Double desiredValue,
    int period,
    Double installmentsTotalAmount
) { }
