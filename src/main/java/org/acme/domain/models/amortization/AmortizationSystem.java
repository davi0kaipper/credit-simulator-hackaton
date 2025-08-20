package org.acme.domain.models.amortization;

import java.util.ArrayList;

import org.acme.domain.models.Installment;

public abstract class AmortizationSystem {
    protected double presentValue;
    protected double interestRate;
    protected int period;

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

    public abstract ArrayList<Installment> calculateInstallments();
}
