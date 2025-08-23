package org.acme.domain.models.amortization;

import java.util.ArrayList;

import org.acme.domain.models.Installment;
import org.acme.infrastructure.tables.SimulationResultTable;

public class Price extends AmortizationSystem {
    public Price() { }

    @Override
    public ArrayList<Installment> calculateInstallments(SimulationResultTable simulationResult) {
        ArrayList<Installment> installments = new ArrayList<>();
        var presentValuePivot = this.presentValue;
        var value = this.calculateInstallmentValue();

        for (int i = 1; i <= this.period; i++) {
            var interest = presentValuePivot * interestRate;
            var amortization = value - interest;
            var installment = new Installment(
                i,
                amortization,
                interest,
                value
            );
            installments.add(installment);
            this.persistInstallment(installment, simulationResult);
            presentValuePivot -= amortization;
        }

        return installments;
    }

    public Double calculateInstallmentValue() {
        var power = Math.pow((1 + this.interestRate), period);
        var numerator = power * this.interestRate;
        var denominator = power - 1;
        var installment = this.presentValue * (numerator / denominator);
        return installment;
    }
}
