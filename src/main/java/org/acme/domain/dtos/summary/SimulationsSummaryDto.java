package org.acme.domain.dtos.summary;

import java.time.LocalDate;
import java.util.List;

public record SimulationsSummaryDto(
    LocalDate referenceDate,
    List<SimulationsSummaryByProductDto> simulationsSummary
) { }