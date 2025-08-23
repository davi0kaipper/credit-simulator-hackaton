package org.acme.domain.payloads.loan.simulation;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.acme.domain.dtos.SimulationRecordDto;

public record SimulationRecordResponse(
    Long idSimulacao,
    BigDecimal valorDesejado,
    int prazo,
    BigDecimal valorTotalParcelas
) {
    public static SimulationRecordResponse from(SimulationRecordDto simulationRecordDto) {
        return new SimulationRecordResponse(
            simulationRecordDto.simulationId(),
            SimulationRecordResponse.formatValue(simulationRecordDto.desiredValue()),
            simulationRecordDto.period(),
            SimulationRecordResponse.formatValue(simulationRecordDto.installmentsTotalAmount())
        );
    }

    static BigDecimal formatValue(Double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
