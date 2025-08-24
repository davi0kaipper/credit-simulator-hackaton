package org.acme.domain.entities;

import java.math.BigDecimal;

import org.acme.domain.models.Installment;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "installments")
public class InstallmentEntity extends PanacheEntity {
    public int number;
    public BigDecimal amortization;
    public BigDecimal interest;
    public BigDecimal value;
    @ManyToOne
    @JoinColumn(name = "simulation_result_id")
    public SimulationResultEntity simulationResult;
    
    public InstallmentEntity() { }

    public static InstallmentEntity from(Installment installment, SimulationResultEntity simulationResult) {
        var instance = new InstallmentEntity();
        instance.number = installment.getNumber();
        instance.amortization = installment.getAmortization();
        instance.interest = installment.getInterest();
        instance.value = installment.getValue();
        instance.simulationResult = simulationResult;
        return instance;
    }
}
