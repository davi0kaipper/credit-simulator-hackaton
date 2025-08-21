package org.acme.domain.models.amortization;

import java.util.ArrayList;

import org.acme.domain.models.Installment;
import org.acme.infrastructure.tables.SimulationResultTable;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Sac extends AmortizationSystem{
    public Sac() { }

    @Override
    public ArrayList<Installment> calculateInstallments(SimulationResultTable simulationResult) {
        var installments = new ArrayList<Installment>();
        var presentValuePivot = this.presentValue;
        var amortization = this.presentValue / this.period;

        for (int i = 1; i <= this.period; i++) {
            var interest = presentValuePivot * interestRate;
            var value = amortization + interest;
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
}
