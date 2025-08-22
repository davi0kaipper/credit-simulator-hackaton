package org.acme.domain.payloads.loan.simulation;

import java.time.format.DateTimeFormatter;

import org.acme.domain.dtos.summary.SimulationsSummaryDto;

public record SimulationsSummaryResponse(
    String dataReferencia,
    SimulationsSummaryByProductPayload simulacoes
) {
    public static SimulationsSummaryResponse from(SimulationsSummaryDto simulationsSummary) {
        var data = SimulationsSummaryByProductPayload.from(simulationsSummary.simulationsSummary());
        return new SimulationsSummaryResponse(
            simulationsSummary.referenceDate().format(DateTimeFormatter.ISO_DATE),
            data
        );
    }
}