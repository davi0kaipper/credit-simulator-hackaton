package org.acme.domain.payloads.loan.simulation;

public record SimulationRequest(
    Double valorDesejado,
    int prazo
) { }
