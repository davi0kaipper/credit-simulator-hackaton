package org.acme.domain.entities;

import org.acme.domain.enums.SimulationType;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "simulation_results")
public class SimulationResultEntity extends PanacheEntity {
    @Enumerated(EnumType.STRING)
    public SimulationType type;
    @ManyToOne
    @JoinColumn(name = "simulation_id")
    public SimulationEntity simulation;

    public SimulationResultEntity() { }
}
