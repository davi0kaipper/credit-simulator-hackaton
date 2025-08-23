package org.acme.domain.dtos;

import org.acme.infrastructure.tables.InstallmentTable;
import org.acme.infrastructure.tables.SimulationTable;

public record SimulationRecordDto(
    Long simulationId,
    Double desiredValue,
    int period,
    Double installmentsTotalAmount
) {
    public static SimulationRecordDto from(SimulationTable simulation) {
        var installmentsTotalAmount = InstallmentTable
            .getInstallmentsTotalAmountBySimulationId(simulation.id);

        return new SimulationRecordDto(
            simulation.id,
            simulation.desiredValue,
            simulation.period,
            installmentsTotalAmount
        );
    }
}
