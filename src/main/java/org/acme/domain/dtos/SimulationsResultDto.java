package org.acme.domain.dtos;

import java.util.ArrayList;

public record SimulationsResultDto(
        Long simulationId,
        Long productId,
        String productName,
        Double interestRate,
        ArrayList<PresentSimulationDto> simulationResult
) { }
