package org.acme.domain.models;

import java.util.ArrayList;

import org.acme.domain.enums.SimulationType;

public class Simulation {
    public SimulationType type;
    public ArrayList<Installment> installments;
    
    public Simulation(
        SimulationType type,
        ArrayList<Installment> installments
    ){
        this.type = type;
        this.installments = installments;
    }
}
