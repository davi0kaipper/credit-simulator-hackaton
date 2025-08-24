package org.acme.domain.payloads.loan.simulation;

import java.util.List;

import org.acme.domain.dtos.PresentSimulationDto;
import org.acme.domain.enums.SimulationType;

public record PresentSimulationFormat(
    SimulationType tipo,
    List<PresentInstallmentFormat> parcelas
) {
    public static PresentSimulationFormat from(PresentSimulationDto presentSimulationDto) {
        return new PresentSimulationFormat(
            presentSimulationDto.type(),
            presentSimulationDto.installments().stream()
            .map(PresentInstallmentFormat::from).toList()
        );
    }
}
