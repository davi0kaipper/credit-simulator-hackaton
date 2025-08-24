package org.acme.domain.dtos;

import java.util.ArrayList;

import org.acme.domain.enums.SimulationType;

public record PresentSimulationDto(
    SimulationType type,
    ArrayList<PresentInstallmentDto> installments
) { }
