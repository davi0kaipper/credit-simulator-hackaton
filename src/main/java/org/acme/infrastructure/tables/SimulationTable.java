package org.acme.infrastructure.tables;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "simulations")
public class SimulationTable extends PanacheEntity {
    public Double desiredValue;
    public Integer period;
    @ManyToOne
    @JoinColumn(name = "product_id")
    public ProductTable product;
    public LocalDateTime timestamp;

    public SimulationTable() {
        this.timestamp = LocalDateTime.now();
    }

    public double getInstallmentsTotalAmountBySimulationId(Long simulationId) {
        var installmentsTotalAmount = Panache.getEntityManager()
            .createQuery("""
                    SELECT SUM(i.value) FROM InstallmentTable i
                    WHERE i.simulationResult.simulation.id = :simulation_id
                """,
                Double.class
            )
            .setParameter("simulation_id", simulationId)
            .getSingleResult();
        return installmentsTotalAmount;
    }
}
