package org.acme.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "simulations")
public class SimulationEntity extends PanacheEntity {
    public Double desiredValue;
    public Integer period;
    @Column(precision = 10, scale = 9)
    public BigDecimal interestRate;
    public Long productId;
    public LocalDate timestamp;

    public SimulationEntity() {
        this.timestamp = LocalDate.now();
    }
}
