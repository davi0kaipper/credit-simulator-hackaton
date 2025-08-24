package org.acme.domain.models.amortization;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.acme.domain.entities.InstallmentEntity;
import org.acme.domain.entities.SimulationResultEntity;
import org.acme.domain.models.Installment;

import jakarta.transaction.Transactional;

public abstract class AmortizationSystem {
    protected BigDecimal presentValue;
    protected BigDecimal interestRate;
    protected int period;

    public abstract ArrayList<Installment> calculateInstallments(SimulationResultEntity simulationResult);

    @Transactional
    protected void persistInstallment(
        Installment installment,
        SimulationResultEntity simulationResult
    ){
        InstallmentEntity.from(installment, simulationResult).persist();
    }

    public AmortizationSystem setPresentValue(BigDecimal presentValue) {
        this.presentValue = presentValue;
        return this;
    }

    public AmortizationSystem setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    public AmortizationSystem setPeriod(int period) {
        this.period = period;
        return this;
    }
}
