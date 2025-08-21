package org.acme.domain.payloads.loan.simulation;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.acme.domain.dtos.SimulationRecordDto;

public record SimulationRecordPayload(
    Long idSimulacao,
    BigDecimal valorDesejado,
    int prazo,
    BigDecimal valorTotalParcelas
) {
    public static SimulationRecordPayload from(SimulationRecordDto simulationRecordDto) {
        return new SimulationRecordPayload(
            simulationRecordDto.simulationId(),
            SimulationRecordPayload.formatValue(simulationRecordDto.desiredValue()),
            simulationRecordDto.period(),
            SimulationRecordPayload.formatValue(simulationRecordDto.installmentsTotalAmount())
        );
    }

    static BigDecimal formatValue(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
