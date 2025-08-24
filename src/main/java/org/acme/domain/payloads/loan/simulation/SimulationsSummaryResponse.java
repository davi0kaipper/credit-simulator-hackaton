package org.acme.domain.payloads.loan.simulation;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.acme.domain.dtos.summary.SimulationsSummaryDto;

public record SimulationsSummaryResponse(
    String dataReferencia,
    List<SimulationsSummaryByProductPayload> simulacoes
) {
    public static SimulationsSummaryResponse from(SimulationsSummaryDto simulationsSummary) {
        var data = simulationsSummary.simulationsSummary().stream()
            .map(SimulationsSummaryByProductPayload::from).toList();
        return new SimulationsSummaryResponse(
            simulationsSummary.referenceDate().format(DateTimeFormatter.ISO_DATE),
            data
        );
    }
}