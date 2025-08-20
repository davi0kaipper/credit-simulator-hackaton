package org.acme.domain.dtos;

public record SimulationSolicitationDto(
    double desiredValue,
    int period
) { }
