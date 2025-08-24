package org.acme.domain.dtos;

import java.math.BigDecimal;
import java.util.ArrayList;

public record SimulationsResultDto(
        Long simulationId,
        Long productId,
        String productName,
        BigDecimal interestRate,
        ArrayList<PresentSimulationDto> simulationResult
) { }
