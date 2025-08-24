package org.acme.domain.models.amortization;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.acme.domain.entities.SimulationResultEntity;
import org.acme.domain.models.Installment;

public class Price extends AmortizationSystem {
    public Price() { }

    @Override
    public ArrayList<Installment> calculateInstallments(SimulationResultEntity simulationResult) {
        ArrayList<Installment> installments = new ArrayList<>();
        var presentValuePivot = this.presentValue;
        var value = this.calculateInstallmentValue();

        for (int i = 1; i <= this.period; i++) {
            var interest = presentValuePivot.multiply(interestRate);
            var amortization = value.subtract(interest);
            var installment = new Installment(
                i,
                amortization,
                interest,
                value
            );
            installments.add(installment);
            this.persistInstallment(installment, simulationResult);
            presentValuePivot = presentValuePivot.subtract(amortization);
        }

        return installments;
    }

    public BigDecimal calculateInstallmentValue() {
        var power = Math.pow((1 + this.interestRate.doubleValue()), this.period);
        var numerator = BigDecimal.valueOf(power).multiply(this.interestRate);
        var denominator = power - 1;
        var division = numerator.divide(BigDecimal.valueOf(denominator), RoundingMode.HALF_UP);
        var installment = this.presentValue.multiply(division);
        return installment;
    }
}
