package org.acme.domain.payloads.loan.simulation;

public record SimulationRequest(
    double valorDesejado,
    int prazo
) { }
