package org.acme.infrastructure.tables;

import org.acme.domain.models.Installment;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "installments")
public class InstallmentTable extends PanacheEntity {
    public int number;
    public double amortization;
    public double interest;
    public double value;
    @ManyToOne
    @JoinColumn(name = "simulation_result_id")
    public SimulationResultTable simulationResult;
    
    public InstallmentTable() { }

    public static InstallmentTable from(Installment installment, SimulationResultTable simulationResult) {
        var instance = new InstallmentTable();
        instance.number = installment.getNumber();
        instance.amortization = installment.getAmortization();
        instance.interest = installment.getInterest();
        instance.value = installment.getValue();
        instance.simulationResult = simulationResult;
        return instance;
    }
}
