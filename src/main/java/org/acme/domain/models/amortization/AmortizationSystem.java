package org.acme.domain.models.amortization;

import java.util.ArrayList;

import org.acme.domain.models.Installment;
import org.acme.infrastructure.tables.InstallmentTable;
import org.acme.infrastructure.tables.SimulationResultTable;

import jakarta.transaction.Transactional;

public abstract class AmortizationSystem {
    protected double presentValue;
    protected double interestRate;
    protected int period;

    public abstract ArrayList<Installment> calculateInstallments(SimulationResultTable simulationResult);

    @Transactional
    protected void persistInstallment(
        Installment installment,
        SimulationResultTable simulationResult
    ){
        InstallmentTable.from(installment, simulationResult).persist();
    }

    public AmortizationSystem setPresentValue(double presentValue) {
        this.presentValue = presentValue;
        return this;
    }

    public AmortizationSystem setInterestRate(double interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    public AmortizationSystem setPeriod(int period) {
        this.period = period;
        return this;
    }
}
