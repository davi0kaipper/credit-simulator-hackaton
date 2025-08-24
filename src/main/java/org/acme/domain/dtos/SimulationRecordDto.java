package org.acme.domain.dtos;

import java.math.BigDecimal;

public class SimulationRecordDto {
    private Long simulationId;
    private Double desiredValue;
    private int period;
    private Double installmentsTotalAmount;

    public SimulationRecordDto(
        Long simulationId,
        Double desiredValue,
        int period,
        Double installmentsTotalAmount
    ) {
        this.simulationId = simulationId;
        this.desiredValue = desiredValue;
        this.period = period;
        this.installmentsTotalAmount = installmentsTotalAmount;
    }

    public Long getSimulationId() {
        return this.simulationId;
    }

    public BigDecimal getDesiredValue() {
        return BigDecimal.valueOf(this.desiredValue);
    }

    public int getPeriod() {
        return this.period;
    }

    public BigDecimal getInstallmentsTotalAmount() {
        return BigDecimal.valueOf(this.installmentsTotalAmount);
    }
}
