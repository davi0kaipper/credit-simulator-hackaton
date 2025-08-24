package org.acme.domain.models.amortization;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.acme.domain.entities.SimulationResultEntity;
import org.acme.domain.models.Installment;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Sac extends AmortizationSystem{
    public Sac() { }

    @Override
    public ArrayList<Installment> calculateInstallments(SimulationResultEntity simulationResult) {
        var installments = new ArrayList<Installment>();
        var presentValuePivot = this.presentValue;
        var amortization = this.presentValue.divide(BigDecimal.valueOf(this.period), RoundingMode.HALF_UP);

        for (int i = 1; i <= this.period; i++) {
            var interest = presentValuePivot.multiply(interestRate);
            var value = amortization.add(interest);
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
}
