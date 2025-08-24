package org.acme.domain.payloads.loan.simulation;

import java.math.BigDecimal;

import org.acme.domain.dtos.SimulationRecordDto;

public record SimulationRecordResponse(
    Long idSimulacao,
    BigDecimal valorDesejado,
    int prazo,
    BigDecimal valorTotalParcelas
) {
    public static SimulationRecordResponse from(SimulationRecordDto simulationRecordDto) {
        return new SimulationRecordResponse(
            simulationRecordDto.getSimulationId(),
            simulationRecordDto.getDesiredValue(),
            simulationRecordDto.getPeriod(),
            simulationRecordDto.getInstallmentsTotalAmount()
        );
    }
}
