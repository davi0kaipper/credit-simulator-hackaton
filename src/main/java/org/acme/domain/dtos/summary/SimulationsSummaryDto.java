package org.acme.domain.dtos.summary;

import java.time.LocalDate;

public record SimulationsSummaryDto(
    LocalDate referenceDate,
    SimulationsSummaryByProductDto simulationsSummary
) { }