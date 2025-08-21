package org.acme.domain.dtos;

import org.acme.infrastructure.tables.SimulationTable;

public record SimulationRecordDto(
    Long simulationId,
    double desiredValue,
    int period,
    double installmentsTotalAmount
) {
    public static SimulationRecordDto from(SimulationTable simulation) {
        var installmentsTotalAmount = simulation.getInstallmentsTotalAmountBySimulationId(simulation.id);
        return new SimulationRecordDto(
            simulation.id,
            simulation.desiredValue,
            simulation.period,
            installmentsTotalAmount
        );
    }
}
