package org.acme.domain.dtos;

public record SimulationSolicitationDto(
    Double desiredValue,
    int period
) { }
