package org.acme.domain.dtos;

import java.util.ArrayList;

import org.acme.domain.enums.SimulationType;

public record PresentSimulationDto(
    SimulationType tipo,
    ArrayList<PresentInstallmentDto> parcelas
) { }
